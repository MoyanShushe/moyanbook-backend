package com.moyanshushe.utils;

import com.moyanshushe.model.dto.item.ItemForUpdate;
import org.junit.jupiter.api.Test;

import java.io.File;

/*
 * Author: Hacoj
 * Version: 1.0
 */
class JsonToObjectUtilTest {

    @Test
    void testJsonToObject() {
        ItemForUpdate itemForUpdate = JsonToObjectUtil.jsonToObject("{\n" +
                "  \"id\": 2,\n" +
                "  \"name\": \"name\",\n" +
                "  \"price\": 120.00,\n" +
                "  \"description\": \"test\",\n" +
                "  \"status\": 1,\n" +
                "  \"user\": {\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  \"labels\": [\n" +
                "    {\n" +
                "      \"id\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}", ItemForUpdate.class);

        System.out.println(itemForUpdate);
    }

    @Test
    void testJsonFileToObject() {
        ItemForUpdate itemForUpdate = JsonToObjectUtil.jsonFileToObject(new File("src/test/resources/json/ItemForUpdate.json"), ItemForUpdate.class);

        System.out.println(itemForUpdate);
    }
}
