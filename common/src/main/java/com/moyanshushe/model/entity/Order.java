package com.moyanshushe.model.entity;

import org.babyfish.jimmer.jackson.JsonConverter;
import org.babyfish.jimmer.sql.*;

import java.util.*;
import java.time.LocalDateTime;

/**
 * Entity for table "order"
 */
@Entity
@Table(name = "ORDER_DETAIL")
public interface Order {

    /**
     * 订单id
     */
    @Id
    int orderId();

    /**
     * 订单所属用户id
     */
    @IdView
    int userId();

    /**
     * 订单所属用户
     */
    @OneToOne
    @JoinColumn(
            name = "user_id"
    )
    User user();

    /**
     * 订单状态:  0：正常  1：等待支付 2：支付中  3：订单完成
     */
    Short status();

    /**
     * 订单商品
     */
    @OneToMany(mappedBy = "order")
    List<Item> items();

    /**
     * 创建时间
     */
    LocalDateTime createTime();
}

