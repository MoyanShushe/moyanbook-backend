package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.AddressConstant;
import com.moyanshushe.constant.CommonConstant;
import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.address.AddressForDelete;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.address.AddressSubstance;
import com.moyanshushe.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Api
    @PostMapping("/get")
    public ResponseEntity<Result> get (
            @RequestBody AddressForQuery addressForQuery) {

        Page<AddressSubstance> page = addressService.get(addressForQuery);

        return ResponseEntity.ok(
                Result.success(page)
        );
    }

    @Api
    @PostMapping("/add")
    public ResponseEntity<Result> add(
            @RequestBody AddressSubstance addressSubstance) {

        Boolean result = addressService.add(addressSubstance);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result) ? Result.success(AddressConstant.ADDRESS_ADD_SUCCESS) : Result.error(AddressConstant.ADDRESS_ADD_FAILURE)
        );
    }

    @Api
    @PostMapping("/update")
    public ResponseEntity<Result> update(
            @RequestBody AddressSubstance addressSubstance) {

        if (addressSubstance.getId() == null) {
            return ResponseEntity.ok(Result.error(CommonConstant.INPUT_INVALID));
        }

        Boolean result = addressService.update(addressSubstance);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result)
                        ? Result.success(AddressConstant.ADDRESS_UPDATE_SUCCESS)
                        : Result.error(AddressConstant.ADDRESS_UPDATE_FAILURE)
        );
    }

    @Api
    @PostMapping("/delete")
    public ResponseEntity<Result> delete(
            @RequestBody AddressForDelete addressForDelete) {

        Boolean result = addressService.delete(addressForDelete);

        return ResponseEntity.ok(
                Boolean.TRUE.equals(result)
                        ? Result.success(AddressConstant.ADDRESS_DELETE_SUCCESS)
                        : Result.error(AddressConstant.ADDRESS_DELETE_FAILURE)
        );
    }
}
