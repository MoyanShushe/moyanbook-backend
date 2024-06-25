package com.moyanshushe.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import org.babyfish.jimmer.sql.*;


import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity for table "item"
 */
@Entity
//        (microServiceName = "common-service")
public interface Item {

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
    @Nullable
    Double price();

    /**
     * 书籍描述
     */
    @Nullable
    String description();

    /**
     * 书籍状态  0: 正常，1：下架
     */
    Short status();

    /**
     * 书籍创建时间
     */
    @JsonIgnore
    LocalDate createTime();

    /**
     * 书籍更新时间
     */
    @JsonIgnore
    LocalDate updateTime();

    @JsonIgnore
    int updatePersonId();

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
            name = "ITEM_LABEL_MAPPING",
            joinColumnName = "ITEM_ID",
            inverseJoinColumnName = "LABEL_ID"
    )
    List<Label> labels();

    @OneToMany(mappedBy = "item")
    List<ItemImage> images();


    @ManyToOne
    @JoinTable(name = "ORDER_ITEM_MAPPING",
            joinColumnName = "item_id",
            inverseJoinColumnName = "order_id")
    @Null
    Order order();

    @OneToOne(mappedBy = "item")
    @Null
    ItemCode itemCode();

    @ManyToOne
    @JoinTable(
            name = "admin_confirm_item_mapping",
            joinColumnName = "item_id",
            inverseJoinColumnName = "confirm_code"
    )
    @Null
    AdminConfirm adminConfirm();

    class Status {
        public static final short IN_USER = 00;
        public static final short NORMAL = 10;
        public static final short WAIT_FOR_PAYING = 20;
        public static final short WAIT_FOR_SENDING = 30;
        public static final short WAIT_FOR_RECEIVING = 40;
        public static final short FINISHED = 50;

        private Status() {}
    }
}

