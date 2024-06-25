package com.moyanshushe.service.impl;

import com.moyanshushe.mapper.OrderMapper;
import com.moyanshushe.model.dto.order.*;
import com.moyanshushe.model.entity.Item;
import com.moyanshushe.model.entity.Order;
import com.moyanshushe.service.OrderService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.stereotype.Service;

import java.util.Collection;

/*
 * Author: Hacoj
 * Version: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;

    public OrderServiceImpl(OrderMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Boolean add(OrderForAdd order) {

        SimpleSaveResult<Order> result = mapper.add(order);

        return result.getAffectedRowCount(Order.class) == 1;
    }

    @Override
    public Page<Order> query(OrderSpecification specification) {
        return mapper.get(specification);
    }

    @Override
    public Boolean update(OrderForUpdate orderForUpdate) {
        SimpleSaveResult<Order> result = mapper.update(orderForUpdate);

        return result.getAffectedRowCount(Order.class) == 1;
    }

    @Override
    public Boolean delete(OrderForDelete orderForDelete) {
        DeleteResult result = mapper.delete(orderForDelete);

        return result.getAffectedRowCount(Order.class) == orderForDelete.getIds().size()
                && result.getAffectedRowCount(Item.class ) == orderForDelete.getIds().size();
    }
}
