package com.moyanshushe.config;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moyanshushe.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ImmutableModule());
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writeValueAsString(new Result());
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper init error", e);
        }
        return objectMapper;
    }
}
