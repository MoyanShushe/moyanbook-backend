package com.moyanshushe.client;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.item.ItemSpecification;
import com.moyanshushe.model.dto.label.LabelForQuery;
import com.moyanshushe.model.dto.order.OrderSpecification;
import com.moyanshushe.model.entity.Item;
import org.babyfish.jimmer.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "common-service")
public interface CommonServiceClientForAdmin {

    @PostMapping("/item/fetch")
    Page<Item> fetchItems(@RequestBody ItemSpecification specification);

    @PostMapping("/address/get")
    ResponseEntity<Result> getAddress (@RequestBody AddressForQuery addressForQuery);

    @PostMapping("/label/fetch")
    ResponseEntity<Result> queryLabels(@RequestBody LabelForQuery labelForQuery);

    @PostMapping("/order/get")
    Page<Item> getOrder(@RequestBody OrderSpecification specification);

}
