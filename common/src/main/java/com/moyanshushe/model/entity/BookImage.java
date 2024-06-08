package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

/**
 * 书籍图片
 */
@Entity
//        (microServiceName = "common-service")
public interface BookImage {

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
    @JoinColumn(name = "book_id")
    @OnDissociate(DissociateAction.DELETE)
    Book book();

    /**
     * 图片网址
     */
    String imageUrl();
}

