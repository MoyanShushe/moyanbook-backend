package com.moyanshushe.model.entity;

import org.babyfish.jimmer.sql.*;

import java.util.List;

/**
 * 地址表
 */
@Entity
public interface Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    int id();

    /**
     * 详细地址
     */
    String address();


    @ManyToMany(
            mappedBy = "responsibilityArea"
    )
    List<Admin> admin();
}

