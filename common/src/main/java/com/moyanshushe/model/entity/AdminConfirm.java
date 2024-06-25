package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;
import java.util.List;

/**
 * Entity for table "admin_confirm"
 */
@Entity
public interface AdminConfirm {

    /**
     * 主键
     */
    @Id
    int id();

    /**
     * 确定码
     */
    String confirmCode();

    /**
     * 状态
     */
    Integer status();

    /**
     * 操作，接收或者送出
     */
    Integer operation();

    @OneToOne
    @JoinColumn(
            name = "admin_id"
    )
    @Null
    Admin admin();

    @OneToMany(mappedBy = "adminConfirm")
    List<Item> items();

    class Status {
        public static final int WAIT_FOR_CONFIRM = 1;
        public static final int CONFIRMED = 2;
    }
}

