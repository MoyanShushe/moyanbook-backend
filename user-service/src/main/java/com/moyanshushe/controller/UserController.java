package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.JwtClaimsConstant;
import com.moyanshushe.constant.UserConstant;
import com.moyanshushe.constant.VerifyConstant;
import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.user.*;
import com.moyanshushe.properties.JwtProperties;
import com.moyanshushe.service.UserService;
import com.moyanshushe.utils.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.client.meta.Api;
import org.babyfish.jimmer.sql.ast.tuple.Tuple2;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.moyanshushe.utils.UserThreadLocalUtil.THREAD_LOCAL_USER_ID;

@Api
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProperties jwtProperties;

    // 构造函数：注入UserService和JwtProperties
    public UserController(UserService userService, JwtProperties jwtProperties) {

        this.userService = userService;
        this.jwtProperties = jwtProperties;

        log.info("UserController initialized:");
    }

    /**
     * 用户注册接口
     *
     * @param userForRegister 包含注册所需信息的对象
     * @return 注册成功返回200和成功信息，失败返回400和失败信息
     */
    @Api
    @PostMapping("/register")
    public ResponseEntity<Result> registerUser(
            @RequestBody UserForRegister userForRegister) {

        log.info("user register: {}", userForRegister.toString());

        // 调用userService进行用户注册
        long userId = userService.userRegister(userForRegister);
        if (userId != -1) {

            // 注册成功
            return ResponseEntity.ok(Result.success(UserConstant.USER_REGISTER_SUCCESS));
        } else {

            // 注册失败
            return ResponseEntity.badRequest().body(Result.error(UserConstant.USER_REGISTER_FAILURE));
        }
    }

    /**
     * 用户登录接口
     *
     * @param userForLogin 包含登录所需信息的对象
     * @return 登录成功返回200和成功信息，失败返回401和失败信息
     */
    @Api
    @PostMapping("/login")
    public ResponseEntity<Result> loginUser(
            @RequestBody UserForLogin userForLogin) {

        log.info("user login: id: {}, name: {}, email: {}, phone: {}",
                userForLogin.getId(), userForLogin.getName(), userForLogin.getEmail(), userForLogin.getPhone());

        // 调用userService进行用户登录验证
        long userId = userService.userLogin(userForLogin);
        if (userId != -1) {
            // 登录成功，生成JWT并返回
            HashMap<String, Object> map = new HashMap<>();
            map.put(JwtClaimsConstant.USER_ID, userId);
            String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), 36000000L, map);

            return ResponseEntity.ok(
                    Result.success(
                            new Tuple2<>(UserConstant.USER_LOGIN_SUCCESS, jwt)));

        } else {
            // 登录失败
            return ResponseEntity.status(401).body(
                            Result.error(UserConstant.USER_LOGIN_FAILURE));
        }
    }

    /**
     * 用户更新接口
     *
     * @param userForUpdate 包含修改信息的对象
     * @return 修改成功返回200和成功信息，失败返回400和失败信息
     */
    @Api
    @PostMapping("/change")
    public ResponseEntity<Result> update(
            @RequestBody UserForUpdate userForUpdate) {

        log.info("user update: {}", userForUpdate.getId());

        // 调用userService进行密码修改
        boolean isChanged = userService.userUpdate(userForUpdate);
        if (isChanged) {
            // 修改成功
            return ResponseEntity.ok(Result.success(UserConstant.USER_CHANGE_SUCCESS));
        } else {
            // 修改失败
            return ResponseEntity.badRequest().body(Result.error(UserConstant.USER_CHANGE_FAILURE));
        }
    }

    @Api
    @PostMapping("/bind")
    public ResponseEntity<Result> bind(UserForBinding userForBinding) {

        log.info("user: {} binding", userForBinding.getId());

        boolean bindSuccess = userService.bind(userForBinding);

        if (bindSuccess) {
            return ResponseEntity.ok(Result.success(UserConstant.USER_BIND_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(Result.error(UserConstant.USER_BIND_FAILURE));
        }
    }

    /**
     * 用户登出接口
     * <p>
     * 该接口用于处理用户的登出请求。当用户请求登出时，会清除当前线程中的用户ID，
     * 以实现用户会话的终止。
     *
     * @return ResponseEntity<Result> 返回一个响应实体，包含登出操作的结果信息。
     */
    @Api
    @PostMapping("/logout")
    public ResponseEntity<Result> logout(
            @RequestParam Long id
    ) {

        log.info("user: {} logout", id);

        // 清除线程中的用户ID，实现用户登出
        THREAD_LOCAL_USER_ID.remove();

        // 返回成功响应，告知用户登出操作成功
        return ResponseEntity.ok().body(Result.success(UserConstant.USER_LOGOUT_SUCCESS));
    }

    /**
     * 用户验证接口
     *
     * @param userForVerify 包含验证信息的对象
     * @return 验证成功返回200和成功信息，失败返回相应的错误信息
     */
    @Api
    @PostMapping("/verify")
    public ResponseEntity<Result> verify(
            @RequestBody UserForVerify userForVerify) {

        log.info("user verify: {}", userForVerify.toString());

        // 调用userService进行用户验证
        userService.userVerify(userForVerify);

        return ResponseEntity.ok().body(Result.success(VerifyConstant.VERIFY_CODE_SENT));
    }
}
