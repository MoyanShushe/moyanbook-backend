package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

/**
 * 物件码
 */
@Entity
public interface ItemCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 地址
     */
    Integer codePart1();

    /**
     * 编码
     */
    Integer codePart2();

    /**
     * 对应的物品id
     */
    @IdView
    Integer itemId();

    @OneToOne
    @Null
    @OnDissociate(DissociateAction.SET_NULL)
    Item item();
}

