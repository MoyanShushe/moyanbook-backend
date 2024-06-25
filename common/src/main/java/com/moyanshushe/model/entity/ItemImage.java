package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

/**
 * 书籍图片
 */
@Entity
//        (microServiceName = "common-service")
public interface ItemImage {

    /**
     * 书籍图片id，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 书籍id
     */
    @Key
    @ManyToOne()
    @JoinColumn(name = "item_id")
    @OnDissociate(DissociateAction.DELETE)
    Item item();

    /**
     * 图片网址
     */
    String imageUrl();
}

