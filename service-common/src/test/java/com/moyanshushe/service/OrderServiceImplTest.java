package com.moyanshushe.service;

/*
 * Author: Hacoj
 * Version: 1.0
 */


import com.moyanshushe.model.dto.order.OrderForAdd;
import com.moyanshushe.model.dto.order.OrderForDelete;
import com.moyanshushe.model.dto.order.OrderForUpdate;
import com.moyanshushe.model.dto.order.OrderSpecification;
import com.moyanshushe.model.entity.Item;
import com.moyanshushe.utils.JsonToObjectUtil;
import org.babyfish.jimmer.Page;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {
    @Autowired
    private OrderService orderService;

    @Test
    @Order(1)
    void testAdd() {
        List<OrderForAdd> orderForAdds = JsonToObjectUtil.jsonFileToObject("OrderForAdd",
                JsonToObjectUtil.JSON_TYPE_FACTORY.constructCollectionType(List.class, OrderForAdd.class));

        orderForAdds.forEach(orderForAdd -> {
            orderService.add(orderForAdd);
        });

        Page<com.moyanshushe.model.entity.Order> page = orderService.query(new OrderSpecification());
        assertEquals(12, page.getTotalRowCount());
    }

    @Test
    @Order(2)
    void testQuery() {
        OrderSpecification specification = new OrderSpecification();
        specification.setStatus(com.moyanshushe.model.entity.Order.Status.NORMAL);
        Page<com.moyanshushe.model.entity.Order> page = orderService.query(specification);

        assertEquals(6, page.getTotalRowCount());

        specification.setUserId(1);
        page = orderService.query(specification);

        assertEquals(2, page.getTotalRowCount());
    }

    @Test
    @Order(3)
    void testUpdate() {
        List<OrderForUpdate> orderForUpdates = JsonToObjectUtil.jsonFileToObject(
                "OrderForUpdate",
                JsonToObjectUtil.JSON_TYPE_FACTORY.constructCollectionType(List.class, OrderForUpdate.class)
        );

        orderForUpdates.forEach(orderForUpdate -> {
            orderService.update(orderForUpdate);
        });

        List<UUID> uuidList = orderForUpdates.stream().map(OrderForUpdate::getId).toList();

        Page<com.moyanshushe.model.entity.Order> page = orderService.query(new OrderSpecification());

        assertEquals(12, page.getTotalRowCount());

        OrderSpecification specification = new OrderSpecification();
        specification.setPageSize(20);

        page = orderService.query(specification);

        page.getRows().forEach(order -> {
            if (uuidList.contains(order.id())) {
                List<OrderForUpdate> list = orderForUpdates
                        .stream()
                        .filter(orderForUpdate -> orderForUpdate.getId().equals(order.id()))
                        .toList();

                OrderForUpdate first = list.getFirst();
                assertEquals(first.getUserId(), order.userId());
                assertEquals(first.getUpdatePersonId(), order.updatePersonId());

                for (int i = 0; i < first.getItems().size(); i++) {
                    try {
                        Item item = order.items().getFirst();
                    } catch (Exception ignored) {
                        continue;
                    }

                    assertEquals(first.getItems().get(i).getId(), order.items().get(i).id());

                }
            }
        });
    }

    @Test
    @Order(4)
    void testDelete() {

        OrderSpecification orderSpecification = new OrderSpecification();

        Page<com.moyanshushe.model.entity.Order> page = orderService.query(orderSpecification);

        long pageCount = page.getTotalPageCount();

        for (int i = 0; i < pageCount; i++) {
            orderSpecification.setPage(0);
            page = orderService.query(orderSpecification);

            List<UUID> list = page.getRows().stream().map(com.moyanshushe.model.entity.Order::id)
                    .toList();

            OrderForDelete orderForDelete = new OrderForDelete();
            orderForDelete.setIds(list);

            orderService.delete(orderForDelete);
        }

        assertEquals(0, orderService.query(new OrderSpecification()).getTotalRowCount());
    }
}
