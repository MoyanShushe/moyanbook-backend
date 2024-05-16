package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;


import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity for table "book"
 */
@Entity
//        (microServiceName = "common-service")
public interface Book {

    /**
     * 书籍id，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 书籍名
     */
    String name();

    /**
     * 书籍价格
     */
    @Null
    Double price();

    /**
     * 书籍描述
     */
    @Null
    String description();

    /**
     * 书籍状态  0: 正常，1：下架
     */
    Short status();

    /**
     * 书籍创建时间
     */
    LocalDate createTime();

    /**
     * 书籍更新时间
     */
    LocalDate updateTime();

    @IdView
    int userId();

    /**
     * 书籍所属用户
     */
    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    User user();

    @ManyToMany()
    @JoinTable(
            name = "BOOK_LABEL_MAPPING",
            joinColumnName = "BOOK_ID",
            inverseJoinColumnName = "LABEL_ID"
    )
    List<Label> labels();

    @OneToMany(mappedBy = "book")
    List<BookImage> images();
}

