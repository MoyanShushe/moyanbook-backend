package com.moyanshushe.service;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.dto.address.AddressForDelete;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.address.AddressSubstance;
import org.babyfish.jimmer.Page;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceImplTest {

    @Autowired
    private AddressService addressService;

    @Test
    @Order(1)
    void testAdd() {
        AddressSubstance substance = new AddressSubstance();
        substance.setAddress("test address");

        Boolean added = addressService.add(substance);
        Boolean added1 = addressService.add(substance);

        assertFalse(added1);
        assertTrue(added);
    }

    @Test
    @Order(2)
    void testQuery() {
        AddressForQuery addressForQuery = new AddressForQuery();
        addressForQuery.setAddress("test address");

        Page<AddressSubstance> page = addressService.get(addressForQuery);
        addressForQuery.setAddress(null);
        Page<AddressSubstance> substancePage = addressService.get(addressForQuery);

        assertEquals(1, page.getTotalRowCount());
        assertEquals(2, substancePage.getTotalRowCount());
    }

    @Test
    @Order(3)
    void testUpdate() {
        AddressSubstance substance = new AddressSubstance();
        substance.setAddress("test address2");

        Boolean added = addressService.add(substance);
        assertTrue(added);

        AddressForQuery addressForQuery = new AddressForQuery();
        addressForQuery.setAddress(substance.getAddress());

        substance.setId(addressService.get(addressForQuery).getRows().getFirst().getId());
        substance.setAddress("test address1");

        Boolean updated = addressService.update(substance);
        assertTrue(updated);

        substance.setAddress("测试地址");
        updated = addressService.update(substance);
        assertTrue(updated);
    }

    @Test
    @Order(4)
    void testDelete() {
        AddressForDelete addressForDelete = new AddressForDelete();
        Page<AddressSubstance> page = addressService.get(new AddressForQuery());
        List<Integer> list = page.getRows().stream().filter(
                addressSubstance -> {
                    assertNotNull(addressSubstance.getId());
                    return addressSubstance.getId() != 1;
                }
        ).map(
                addressSubstance -> {
                    assertNotNull(addressSubstance.getId());
                    return Math.toIntExact(addressSubstance.getId());
                }
        ).toList();

        addressForDelete.setIds(list);

        assertTrue(addressService.delete(addressForDelete));
    }
}
