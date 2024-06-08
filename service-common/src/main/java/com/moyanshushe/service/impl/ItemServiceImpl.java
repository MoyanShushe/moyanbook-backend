package com.moyanshushe.service.impl;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.mapper.ItemMapper;
import com.moyanshushe.model.dto.item.ItemForAdd;
import com.moyanshushe.model.dto.item.ItemForDelete;
import com.moyanshushe.model.dto.item.ItemForUpdate;
import com.moyanshushe.model.dto.item.ItemSpecification;
import com.moyanshushe.model.entity.Item;
import com.moyanshushe.model.entity.Fetchers;
import com.moyanshushe.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper mapper;

    public ItemServiceImpl(ItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @NotNull
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ItemForAdd itemForAdd) {
        log.info("添加书籍: {}", itemForAdd);

        SimpleSaveResult<Item> result = mapper.add(itemForAdd);

        boolean addSuccess =
                result.getTotalAffectedRowCount() ==
                        (itemForAdd.getLabels().size() + itemForAdd.getImages().size() + 1);

        if (addSuccess) {
            log.info("添加书籍成功");
            return true;
        } else {
            log.info("添加书籍失败");
            return false;
        }
    }

    @Override
    @NotNull
    public Boolean delete(ItemForDelete itemForDelete) {
        log.info("删除书籍: {}", itemForDelete.getIds());

        Integer delete = mapper.delete(itemForDelete);

        log.info("删除书籍成功: {}", delete);
        return true;
    }

    @Override
    @NotNull
    public Boolean update(ItemForUpdate itemForUpdate) {
        log.info("更新书籍: {}", itemForUpdate.getId());

        SimpleSaveResult<Item> result = mapper.update(itemForUpdate);

        int totalAffectedRowCount = result.getTotalAffectedRowCount();
        if (totalAffectedRowCount >= 1) {
            log.info("更新书籍成功");

            log.info("from {} \n to \n {}", result.getOriginalEntity(), result.getModifiedEntity());
            return true;
        }

        return false;
    }

    @Override
    public Page<Item> fetch(ItemSpecification specification) {
        log.info("查询书籍: {}", specification);
        return mapper.fetch(specification, Fetchers.ITEM_FETCHER.allScalarFields());
    }
}
