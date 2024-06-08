package com.moyanshushe.service;

import com.moyanshushe.model.dto.order.OrderForAdd;
import com.moyanshushe.model.dto.order.OrderForDelete;
import com.moyanshushe.model.dto.order.OrderForUpdate;
import com.moyanshushe.model.dto.order.OrderSpecification;
import com.moyanshushe.model.entity.Order;
import org.babyfish.jimmer.Page;

import javax.validation.constraints.NotNull;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface OrderService {
    @NotNull
    Boolean add(OrderForAdd order);

    @NotNull
    Page<Order> get(OrderSpecification specification);

    @NotNull
    Boolean updateOrder(OrderForUpdate orderForUpdate);

    @NotNull
    Boolean deleteOrder(OrderForDelete orderForDelete);
}
