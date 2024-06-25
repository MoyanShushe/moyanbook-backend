package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.WebIOConstant;
import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.item.ItemForConfirm;
import com.moyanshushe.model.entity.AdminConfirm;
import com.moyanshushe.service.AdminWorkService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api
@Slf4j
@RestController
@RequestMapping({"/admin-work"})
public class AdminWorkController {

    private final AdminWorkService adminWorkService;

    public AdminWorkController(AdminWorkService adminWorkService) {
        this.adminWorkService = adminWorkService;
    }

    @GetMapping("/query-by-queryid/{queryId}")
    public ResponseEntity<Result> queryByQueryId (
            @PathVariable String queryId
    ) {
        Collection<AdminConfirm> list = adminWorkService.queryItems(queryId);

        return ResponseEntity.ok(Result.success(list));
    }

    @PostMapping()
    public ResponseEntity<Result> remind() {
        adminWorkService.remindToReceiveItem();

        return ResponseEntity.ok(Result.success());
    }

    @PostMapping("/confirm-receive")
    public ResponseEntity<Result> confirmReceive(
            @RequestParam String confirmCode
    ) {
        adminWorkService.confirmToReceive(confirmCode);

        return ResponseEntity.ok(Result.success());
    }

    @PostMapping("/deliver")
    public ResponseEntity<Result> deliver (
            @RequestBody ItemForConfirm itemForConfirm
            ) {
        if (itemForConfirm.getAdminConfirm() == null) {
            return ResponseEntity.badRequest().body(Result.error(WebIOConstant.INPUT_INVALID));
        }

        adminWorkService.confirmToDeliver(itemForConfirm);

        return ResponseEntity.ok(Result.success());
    }
}
