package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

/**
 * 地址表
 */
@Entity
public interface Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    long id();

    /**
     * 详细地址
     */
    String address();
}

