package com.moyanshushe.utils;

/*
 * Author: Hacoj
 * Version: 1.0
 */


import com.moyanshushe.model.dto.item.ItemSubstance;
import com.moyanshushe.model.dto.itemcode.ItemCodeSubstance;
import com.moyanshushe.model.dto.user.UserForVerify;
import com.moyanshushe.model.entity.Item;
import com.moyanshushe.model.entity.ItemCode;
import org.junit.jupiter.api.Test;

public class ItemCodeGeneratorTest {
    @Test
    public void test() {
        ItemCodeGenerator.init(100000);

//        UserForVerify user = new UserForVerify();
//        user.setId(1);

        ItemSubstance itemSubstance = new ItemSubstance();
        itemSubstance.setId(1);
        itemSubstance.setName("test");
        itemSubstance.setUser(new ItemSubstance.TargetOf_user());

        itemSubstance.setPrice(1.0);
        itemSubstance.getUser().setId(1);
        itemSubstance.getUser().setName("test");
        itemSubstance.getUser().setAddress(new ItemSubstance.TargetOf_user.TargetOf_address());
        itemSubstance.getUser().getAddress().setAddress("Http");
        itemSubstance.getUser().getAddress().setId(10);

        ItemCodeSubstance itemCode =  new ItemCodeSubstance();
        Item entity = itemSubstance.toEntity();
        ItemCodeGenerator.generateItemCode(entity, itemCode);

        System.out.println(itemCode);

    }
}
