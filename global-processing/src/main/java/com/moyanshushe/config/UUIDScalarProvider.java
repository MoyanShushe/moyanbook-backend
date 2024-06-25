package com.moyanshushe.config;

import org.babyfish.jimmer.sql.runtime.ScalarProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/*
 * Author: Hacoj
 * Version: 1.0
 */

@Component
public class UUIDScalarProvider extends ScalarProvider<UUID, String> {

    @Override
    public UUID toScalar(@NotNull String sqlValue) {
        return UUID.fromString(sqlValue);
    }

    @Override
    public String toSql(UUID scalarValue) {
        return scalarValue.toString();
    }
}