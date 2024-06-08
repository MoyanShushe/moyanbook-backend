package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */


import com.moyanshushe.client.CommonServiceClientForAdmin;
import com.moyanshushe.constant.AccountConstant;
import com.moyanshushe.constant.JwtClaimsConstant;
import com.moyanshushe.constant.VerifyConstant;
import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.admin.*;
import com.moyanshushe.model.dto.item.ItemSpecification;
import com.moyanshushe.model.dto.label.LabelForQuery;
import com.moyanshushe.model.dto.order.OrderSpecification;
import com.moyanshushe.model.entity.Admin;
import com.moyanshushe.properties.JwtProperties;
import com.moyanshushe.service.AdminService;
import com.moyanshushe.utils.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.client.meta.Api;
import org.babyfish.jimmer.sql.ast.tuple.Tuple2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api
@Slf4j
@RestController
@RequestMapping({"/admin"})
public class AdminController {
    private final AdminService adminService;
    private final JwtProperties jwtProperties;
    private final CommonServiceClientForAdmin commonServiceClientForAdmin;

    // 构造函数：初始化用户服务和JWT属性
    public AdminController(AdminService adminService,
                          JwtProperties jwtProperties,
                           CommonServiceClientForAdmin commonServiceClientForAdmin) {
        this.adminService = adminService;
        this.jwtProperties = jwtProperties;
        this.commonServiceClientForAdmin = commonServiceClientForAdmin;

        log.info("AdminController initialized");
    }

    /**
     * 注册用户
     *
     * @param adminForRegister 用户注册信息
     * @return 注册成功返回200和成功消息，失败返回400和错误消息
     */
    @Api
    @PostMapping({"/register"})
    public ResponseEntity<Result> registerAdmin(@RequestBody AdminForRegister adminForRegister) {
        log.info("admin register: {}", adminForRegister);

        boolean success =
                this.adminService.adminRegister(adminForRegister);

        return success ?
                ResponseEntity.ok(Result.success(AccountConstant.ACCOUNT_REGISTER_SUCCESS))
                : ResponseEntity.badRequest().body(Result.error(AccountConstant.ACCOUNT_REGISTER_FAILURE));
    }

    /**
     * 用户登录
     *
     * @param adminForLogin 用户登录信息
     * @return 登录成功返回200和包含JWT的成功消息，失败返回401和错误消息
     */
    @Api
    @PostMapping({"/login"})
    public ResponseEntity<Result> loginAdmin(@RequestBody AdminForLogin adminForLogin) {

        log.info("admin login: id: {}, name: {}, email: {}, phone: {}"
                , adminForLogin.getId(), adminForLogin.getName(), adminForLogin.getEmail(), adminForLogin.getPhone());

        Admin admin = this.adminService.adminLogin(adminForLogin);
        if (admin != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(JwtClaimsConstant.ADMIN_ID, admin.id());
            String jwt = JwtUtil.createJWT(this.jwtProperties.getAdminSecretKey(), this.jwtProperties.getAdminTtl(), map);

            return ResponseEntity.ok(Result.success(new Tuple2<>(admin, jwt)));

        } else {
            return ResponseEntity.status(401).body(Result.error(AccountConstant.ACCOUNT_LOGIN_FAILURE));
        }

    }

    /**
     * 更新用户信息
     *
     * @param adminForUpdate 用户更新信息
     * @return 更新成功返回200和成功消息，失败返回400和错误消息
     */
    @Api
    @PostMapping({"/update"})
    public ResponseEntity<Result> update(@RequestBody AdminForUpdate adminForUpdate) {
        log.info("admin update: {}", adminForUpdate.getId());

        boolean isChanged = this.adminService.adminUpdate(adminForUpdate);

        return isChanged
                ? ResponseEntity.ok(Result.success(AccountConstant.ACCOUNT_CHANGE_SUCCESS))
                : ResponseEntity.badRequest().body(Result.error(AccountConstant.ACCOUNT_CHANGE_FAILURE));
    }

    @Api
    @PostMapping({"/change-password"})
    public ResponseEntity<Result> changePassword(@RequestBody AdminForUpdatePassword adminForUpdatePassword) {
        log.info("admin change password: {}", adminForUpdatePassword.getId());

//        boolean isUpdated = this.adminService.adminUpdatePassword(adminForUpdatePassword);

//        return isUpdated
//                ? ResponseEntity.ok(Result.success(AccountConstant.USER_CHANGE_SUCCESS))
//                : ResponseEntity.badRequest().body(Result.error(AccountConstant.USER_CHANGE_FAILURE));
        return ResponseEntity.ok(Result.success(Result.success(AccountConstant.ACCOUNT_CHANGE_SUCCESS)));
    }

    /**
     * 绑定用户信息
     *
     * @param adminForBinding 用户绑定信息
     * @return 绑定成功返回200和成功消息，失败返回400和错误消息
     */
    @Api
    @PostMapping({"/bind"})
    public ResponseEntity<Result> bind(AdminForBinding adminForBinding) {

        log.info("admin: {} binding", adminForBinding.getId());

//        boolean bindSuccess = this.adminService.bind(adminForBinding);

//        return bindSuccess
//                ? ResponseEntity.ok(Result.success(AccountConstant.USER_BIND_SUCCESS))
//                : ResponseEntity.badRequest().body(Result.error(AccountConstant.USER_BIND_FAILURE));
        return ResponseEntity.ok(Result.success(Result.success(AccountConstant.ACCOUNT_BIND_SUCCESS)));
    }

    /**
     * 用户登出
     *
     * @param id 用户ID
     * @return 登出成功返回200和成功消息
     */
    @Api
    @PostMapping({"/logout"})
    public ResponseEntity<Result> logout(@RequestParam Long id) {
        log.info("admin: {} logout", id);

        return ResponseEntity.ok().body(Result.success(AccountConstant.ACCOUNT_LOGOUT_SUCCESS));
    }

    /**
     * 验证用户信息
     *
     * @param adminForVerify 用户验证信息
     * @return 验证成功返回200和成功消息
     */
    @Api
    @PostMapping({"/verify"})
    public ResponseEntity<Result> verify(@RequestBody AdminForVerify adminForVerify) {
        log.info("admin verify: {}", adminForVerify);
        this.adminService.adminVerify(adminForVerify);
        return ResponseEntity.ok().body(Result.success(VerifyConstant.VERIFY_CODE_SENT));
    }

    /**
     * 根据指定的物品规格获取物品信息。
     *
     * @param specification 物品规格详情
     * @return 返回物品查询结果
     */
    @Api
    @PostMapping("/item/fetch")
    public ResponseEntity<Result> fetchItem(@RequestBody ItemSpecification specification) {
        return ResponseEntity.ok().body(Result.success(commonServiceClientForAdmin.fetchItems(specification)));
    }


    /**
     * 根据指定条件获取标签信息。
     *
     * @param label 查询标签的条件
     * @return 返回标签查询结果
     */
    @Api
    @PostMapping("/label/fetch")
    public ResponseEntity<Result> fetchLabels(@RequestBody LabelForQuery label) {
        return ResponseEntity.ok().body(Result.success(commonServiceClientForAdmin.queryLabels(label)));
    }

    /**
     * 根据指定条件获取地址信息。
     *
     * @param addressForQuery 查询地址的条件
     * @return 返回地址查询结果
     */
    @Api
    @PostMapping("/address/get")
    public ResponseEntity<Result> getAddress(@RequestBody AddressForQuery addressForQuery) {
        return ResponseEntity.ok().body(Result.success(commonServiceClientForAdmin.getAddress(addressForQuery)));
    }

    /**
     * 根据指定条件获取订单信息。
     *
     * @param specification 查询订单的条件
     * @return 返回订单查询结果
     */
    @Api
    @PostMapping("/order/fetch")
    public ResponseEntity<Result> getOrder(@RequestBody OrderSpecification specification) {
        return ResponseEntity.ok().body(Result.success(commonServiceClientForAdmin.getOrder(specification)));
    }
}
