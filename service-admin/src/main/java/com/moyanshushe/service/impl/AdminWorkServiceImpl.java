package com.moyanshushe.service.impl;

import com.moyanshushe.constant.ItemConstant;
import com.moyanshushe.exception.UnexpectedException;
import com.moyanshushe.exception.work.ConfirmCodeInvaildException;
import com.moyanshushe.exception.work.ItemStatusException;
import com.moyanshushe.model.dto.adminconfirm.AdminConfirmSubstanceInput;
import com.moyanshushe.model.dto.item.ItemForConfirm;
import com.moyanshushe.model.dto.item.ItemUpdateStatus;
import com.moyanshushe.model.dto.itemcode.ItemCodeSubstance;
import com.moyanshushe.model.entity.*;
import com.moyanshushe.properties.PurchaseProperty;
import com.moyanshushe.service.AdminWorkService;
import com.moyanshushe.utils.ItemCodeGenerator;
import com.moyanshushe.utils.RandomUtil;
import com.moyanshushe.utils.verify.MailUtil;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/*
 * Author: Hacoj
 * Version: 1.0
 */


@Service
public class AdminWorkServiceImpl implements AdminWorkService {

    private final MailUtil mailUtil;
    private final JSqlClient jsqlClient;
    private final ItemTable itemTable;
    private final AdminTable adminTable;
    private final AdminConfirmTable adminConfirmTable;
    private final PurchaseProperty adminWorkProperty;
    private final OrderTable orderTable;
    private final UserTable userTable;

    public AdminWorkServiceImpl(
            MailUtil mailUtil,
            JSqlClient jsqlClient,
            PurchaseProperty adminWorkProperty) {

        this.mailUtil = mailUtil;
        this.jsqlClient = jsqlClient;
        this.adminWorkProperty = adminWorkProperty;
        this.itemTable = ItemTable.$;
        this.adminTable = AdminTable.$;
        this.adminConfirmTable = AdminConfirmTable.$;
        this.orderTable = OrderTable.$;
        this.userTable = UserTable.$;
    }

    /**
     * 提醒接收物品的函数。
     * 该函数旨在实现提醒管理员接收特定物品的功能。
     */
    @Override
    @Scheduled(cron = "0 0 4 * * *")
    public void remindToReceiveItem() {
        // 查询当前书籍是否达到阈值，或者时间到期
        // 时间检测
        boolean remind = DayOfWeek.from(LocalDate.now()) == DayOfWeek.SATURDAY;

        List<Admin> normalAdminList = jsqlClient.createQuery(adminTable)
                .where(
                        adminTable.status().eq(Admin.Status.NORMAL)
                ).select(
                        adminTable.fetch(
                                Fetchers.ADMIN_FETCHER
                                        .responsibilityArea()
                                        .email()
                                        .name()
                                        .phone())
                ).execute();

        // 遍历管理员列表，查询收集的书籍信息
        for (Admin admin : normalAdminList) {
            if (admin.responsibilityArea().isEmpty()) {
                continue;
            }
            for (Address address : admin.responsibilityArea()) {
                List<Item> itemList = jsqlClient.createQuery(itemTable)
                        .where(
                                itemTable.user().addressId().eq(address.id())
                        ).where(
                                itemTable.status().eq(Item.Status.IN_USER)
                        ).select(
                                itemTable.fetch(
                                        Fetchers.ITEM_FETCHER
                                                .name()
                                )
                        ).execute();

                // 未达到，直接退出
                if (itemList.isEmpty()) {
                    continue;
                    // 达到，给管理员发送消息
                } else if (itemList.size() > adminWorkProperty.getItemGetThreshold()) {
                    remind = true;
                }

                // 配置物件号
                for (Item item : itemList) {
                    ItemCodeSubstance itemCodeSubstance = new ItemCodeSubstance();
                    ItemCodeGenerator.generateItemCode(item, itemCodeSubstance);

                    // 保存对应好的物件号
                    jsqlClient.save(itemCodeSubstance.toEntity());
                }

                String confirmPath = RandomUtil.randomUrlPath(admin.id(), LocalDate.now());

                AdminConfirmSubstanceInput adminConfirmSubstanceInput = new AdminConfirmSubstanceInput();
                adminConfirmSubstanceInput.setAdmin(new AdminConfirmSubstanceInput.TargetOf_admin());
                adminConfirmSubstanceInput.getAdmin().setId(admin.id());
                adminConfirmSubstanceInput.setConfirmCode(confirmPath);
                adminConfirmSubstanceInput.setStatus(AdminConfirm.Status.WAIT_FOR_CONFIRM);
                adminConfirmSubstanceInput.setItems(itemList.stream().map(item -> {
                    AdminConfirmSubstanceInput.TargetOf_items targetOfItems = new AdminConfirmSubstanceInput.TargetOf_items();
                    targetOfItems.setId(item.id());
                    return targetOfItems;
                }).toList());

                jsqlClient.save(adminConfirmSubstanceInput);

                // 消息：提醒管理员接收书籍，书籍数量，名称，地点
                if (remind) {
                    // 发送提醒消息
                    mailUtil.sendMail(
                            admin.email(),
                            "书籍接收提醒",
                            "您有书籍需要接收，请及时处理。详情请见："  // TODO 添加地址生成逻辑
                    );
                }
            }
        }
    }

    @Override
    public List<AdminConfirm> queryItems(String queryId) {
        List<AdminConfirm> list = jsqlClient.createQuery(adminConfirmTable)
                .where(
                        adminConfirmTable.confirmCode().eq(queryId)
                )
                .select(
                        adminConfirmTable.fetch(
                                Fetchers.ADMIN_CONFIRM_FETCHER
                                        .admin(
                                                Fetchers.ADMIN_FETCHER
                                                        .name()
                                                        .profileUrl()
                                                        .status()
                                        ).status())
                ).execute();

        if (list.isEmpty()) {
            throw new ConfirmCodeInvaildException();
        }

        if (list.size() > 1) {
            throw new UnexpectedException();
        }

        return list;
    }

    @Override
    public void confirmToReceive(String confirmCode) {
        List<AdminConfirm> list = jsqlClient.createQuery(adminConfirmTable)
                .where(adminConfirmTable.confirmCode().eq(confirmCode))
                .select(
                        adminConfirmTable.fetch(
                                Fetchers.ADMIN_CONFIRM_FETCHER
                                        .items(Fetchers.ITEM_FETCHER
                                                .status()
                                        )
                        )
                ).execute();

        if (list.isEmpty()) {
            throw new ConfirmCodeInvaildException();
        }

        if (list.size() > 2) {
            throw new UnexpectedException();
        }

        Collection<Item> items = list.getFirst().items();

        List<ItemUpdateStatus> itemsForUpdateStatus = items.stream().map(item -> {
            if (item.status() != Item.Status.IN_USER) {
                throw new ItemStatusException(
                        ItemConstant.ITEM_STATUS_EXCEPTION +
                                ": " +
                                item.id() +
                                "(status: " +
                                item.status() +
                                ")");
            }

            return new ItemUpdateStatus();
        }).toList();

        jsqlClient.update(itemsForUpdateStatus);

        jsqlClient.createUpdate(adminConfirmTable)
                .set(adminConfirmTable.status(), AdminConfirm.Status.CONFIRMED)
                .where(adminConfirmTable.id().eq(list.getFirst().id()));
    }

    @Override
    public void confirmToDeliver(ItemForConfirm itemForConfirm) {
        int id = itemForConfirm.getAdminConfirm().getAdmin().getId();

        Collection<Integer> list = jsqlClient.createQuery(adminConfirmTable)
                .where(
                        adminConfirmTable.admin().id().eq(id))
                .select(adminConfirmTable.id())
                .execute();

        if (list.size() != 1) {
            throw new ConfirmCodeInvaildException();
        }

        jsqlClient.createUpdate(adminConfirmTable)
                .set(adminConfirmTable.status(), AdminConfirm.Status.CONFIRMED);
    }

    @Override
    @Scheduled(cron = "0 0 4 * * *")
    public void remindToDeliver() {
        boolean remind;

        remind = DayOfWeek.from(LocalDate.now()) == DayOfWeek.SATURDAY;

        // TODO 待优化
        Collection<User> list = jsqlClient.createQuery(userTable)
                .groupBy(userTable.addressId())
                .groupBy(userTable.asTableEx().order().items().adminConfirmId())
                .select(userTable.fetch(
                        Fetchers.USER_FETCHER
                                .address()
                                .order(
                                        Fetchers.ORDER_FETCHER
                                                .items(
                                                        Fetchers.ITEM_FETCHER
                                                                .name()
                                                                .status()
                                                                .images(Fetchers.ITEM_IMAGE_FETCHER
                                                                        .imageUrl())
                                                                .adminConfirm(
                                                                        Fetchers.ADMIN_CONFIRM_FETCHER
                                                                                .admin()
                                                                )
                                                )
                                                .status()
                                )
                )).distinct().execute();

        HashMap<Integer, List<Item>> itemListHashMap = new HashMap<>();

        list.forEach(user -> {
            user.items().forEach(item -> {
                if (itemListHashMap.get(item.adminConfirm().admin().id()) != null) {
                    itemListHashMap.put(item.adminConfirm().admin().id(), new ArrayList<>());
                } else {
                    itemListHashMap.get(item.adminConfirm().admin().id()).add(item);
                }
            });
        });

        for (Integer adminId : itemListHashMap.keySet()) {
            remind = (remind && !itemListHashMap.get(adminId).isEmpty()) || itemListHashMap.get(adminId).size() > adminWorkProperty.getItemDeliverThreshold();
            // 消息：提醒管理员接收书籍，书籍数量，名称，地点
            if (remind) {
                // 发送提醒消息
                mailUtil.sendMail(
                        jsqlClient.createQuery(adminTable).where(adminTable.id().eq(adminId)).select(adminTable.email()).execute().getFirst(),
                        "书籍配送提醒",
                        "您有书籍需要配送，请及时处理。详情请见："  // TODO 添加地址生成逻辑
                );
            }
        }
    }
}
