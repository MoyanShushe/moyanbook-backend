package com.moyanshushe.client;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.coupon.CouponSpecification;
import com.moyanshushe.model.dto.coupon.CouponSubstance;
import com.moyanshushe.model.dto.item.ItemForAdd;
import com.moyanshushe.model.dto.item.ItemForDelete;
import com.moyanshushe.model.dto.item.ItemForUpdate;
import com.moyanshushe.model.dto.item.ItemSpecification;
import com.moyanshushe.model.dto.label.LabelForQuery;
import com.moyanshushe.model.dto.order.OrderForAdd;
import com.moyanshushe.model.dto.order.OrderForDelete;
import com.moyanshushe.model.dto.order.OrderForUpdate;
import com.moyanshushe.model.dto.order.OrderSpecification;
import com.moyanshushe.model.entity.Item;
import org.babyfish.jimmer.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@FeignClient(value = "common-service",
        qualifiers = "common-service-feign-user")
public interface CommonServiceClient {

    @PostMapping("/item/fetch")
    Page<Item> fetchItems(@RequestBody ItemSpecification specification);

    @PostMapping("/item/add")
    ResponseEntity<Result> addItem(@RequestBody ItemForAdd itemForAdd);

    @PostMapping("/item/update")
    ResponseEntity<Result> updateItem(@RequestBody ItemForUpdate itemForUpdate);

    @PostMapping("/item/delete")
    ResponseEntity<Result> deleteItems(@RequestBody ItemForDelete itemForDelete);

    @PostMapping("/address/get")
    ResponseEntity<Result> getAddress (@RequestBody AddressForQuery addressForQuery);

    @PostMapping("/file/upload/image")
    CompletableFuture<Result> uploadImage(@RequestParam("file") MultipartFile file);

    @PostMapping("/label/fetch")
    ResponseEntity<Result> queryLabels(@RequestBody LabelForQuery labelForQuery);

    @PostMapping("/order/get")
    Page<Item> getOrder(@RequestBody OrderSpecification specification);

    @PostMapping("/order/add")
    ResponseEntity<Result> addOrder(@RequestBody OrderForAdd orderForAdd);

    @PostMapping("/order/update")
    ResponseEntity<Result> updateOrder(@RequestBody OrderForUpdate orderForUpdate);

    @PostMapping("/order/delete")
    ResponseEntity<Result> deleteOrder(@RequestBody OrderForDelete orderForDelete);

    @PostMapping("/coupon/get")
    Page<CouponSubstance> getCoupon(@RequestBody CouponSpecification couponSpecification);
}
