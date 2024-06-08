package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.time.LocalDateTime;

/**
 * Entity for table "coupon"
 */
@Entity
public interface Coupon {

    /**
     * 优惠券id，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 优惠券名
     */
    String name();

    /**
     * 优惠券价格
     */
    @Null
    Object price();

    /**
     * 优惠券描述
     */
    String description();

    /**
     * 优惠券状态  0: 正常，1：下架
     */
    Short status();

    /**
     * 优惠券创建时间
     */
    LocalDateTime createTime();

    /**
     * 优惠券过期时间
     */
    LocalDateTime expirationTime();

    /**
     * 优惠券所属用户id
     */
    @IdView
    int userId();

    @ManyToOne
    User user();
}

