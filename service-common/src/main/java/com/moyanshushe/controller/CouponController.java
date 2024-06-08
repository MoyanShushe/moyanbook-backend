package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.CouponConstant;
import com.moyanshushe.constant.CommonConstant;
import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.coupon.CouponForDelete;
import com.moyanshushe.model.dto.coupon.CouponSpecification;
import com.moyanshushe.model.dto.coupon.CouponSubstance;
import com.moyanshushe.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@Slf4j
@RestController
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @Api
    @PostMapping("/get")
    public ResponseEntity<Result> get (
            @RequestBody CouponSpecification couponSpecification) {

        Page<CouponSubstance> page = service.get(couponSpecification);

        return ResponseEntity.ok(
                Result.success(page)
        );
    }

    @Api
    @PostMapping("/add")
    public ResponseEntity<Result> add(
            @RequestBody CouponSubstance couponSubstance) {

        Boolean result = service.add(couponSubstance);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result) ? Result.success(CouponConstant.COUPON_ADD_SUCCESS) : Result.error(CouponConstant.COUPON_ADD_FAIL)
        );
    }

    @Api
    @PostMapping("/update")
    public ResponseEntity<Result> update(
            @RequestBody CouponSubstance couponSubstance) {

        if (couponSubstance.getId() == null) {
            return ResponseEntity.ok(Result.error(CommonConstant.INPUT_INVALID));
        }

        Boolean result = service.update(couponSubstance);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result)
                        ? Result.success(CouponConstant.COUPON_UPDATE_SUCCESS)
                        : Result.error(CouponConstant.COUPON_UPDATE_FAIL)
        );
    }

    @Api
    @PostMapping("/delete")
    public ResponseEntity<Result> delete(
            @RequestBody CouponForDelete couponForDelete) {

        Boolean result = service.delete(couponForDelete);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result)
                        ? Result.success(CouponConstant.
                        COUPON_DELETE_SUCCESS)
                        : Result.error(CouponConstant.COUPON_DELETE_FAIL)
        );
    }
}
