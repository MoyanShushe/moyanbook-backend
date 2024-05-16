package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity for table "user"
 */
@Entity
//        (microServiceName = "user-service")
public interface User {

    /**
     * 用户id，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 用户名
     */
    String name();

    /**
     * 用户年龄
     */
    @Null
    Integer age();

    /**
     * 用户性别
     */
    @Null
    Short gender();

    /**
     * 用户邮箱
     */
    @Null
    String email();

    /**
     * 用户手机号
     */
    @Null
    String phone();

    /**
     * 用户密码
     */
    String password();

    @OneToMany(mappedBy = "user")
    List<Book> books();

    /**
     * 用户状态  0: 正常，1：冻结，2：过期
     */
    @Null
    Short status();

    /**
     * 用户创建时间
     */
    LocalDate createTime();

    /**
     * 用户更新时间
     */
    LocalDate updateTime();

    /**
     * 头像网址
     */
    @Null
    String profileUrl();

    /**
     * 上一次登录时间
     */
    LocalDate lastLoginTime();
}

