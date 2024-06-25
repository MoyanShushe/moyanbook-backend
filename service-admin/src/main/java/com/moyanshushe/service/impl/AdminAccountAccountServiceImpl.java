package com.moyanshushe.service.impl;

import com.moyanshushe.constant.AccountConstant;
import com.moyanshushe.constant.RedisConstant;
import com.moyanshushe.exception.account.AccountExistsException;
import com.moyanshushe.exception.account.AccountNameErrorException;
import com.moyanshushe.exception.account.AccountPasswordErrorException;
import com.moyanshushe.exception.account.AccountPermissionException;
import com.moyanshushe.model.dto.admin.AdminForLogin;
import com.moyanshushe.model.dto.admin.AdminForRegister;
import com.moyanshushe.model.dto.admin.AdminForUpdate;
import com.moyanshushe.model.dto.admin.AdminForVerify;
import com.moyanshushe.model.entity.Admin;
import com.moyanshushe.model.entity.AdminTable;
import com.moyanshushe.model.entity.Fetchers;
import com.moyanshushe.service.AdminAccountService;
import com.moyanshushe.utils.security.AccountUtil;
import com.moyanshushe.utils.security.SHA256Encryption;
import com.moyanshushe.utils.verify.CaptchaGenerator;
import com.moyanshushe.utils.verify.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.moyanshushe.utils.AdminThreadLocalUtil.THREAD_LOCAL_ADMIN_ID;

/*
 * Author: Hacoj
 * Version: 1.0
 */

@Slf4j
@Service
public class AdminAccountAccountServiceImpl implements AdminAccountService {

    private final MailUtil mailUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final JSqlClient jsqlClient;
    private final AdminTable table;

    public AdminAccountAccountServiceImpl(MailUtil mailUtil, StringRedisTemplate stringRedisTemplate, JSqlClient jsqlClient) {
        this.mailUtil = mailUtil;
        this.stringRedisTemplate = stringRedisTemplate;
        this.jsqlClient = jsqlClient;
        this.table = AdminTable.$;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean adminRegister(AdminForRegister admin) {
        String captchaGet = admin.getCaptcha();

        String captcha = stringRedisTemplate.opsForValue().
                get(RedisConstant.ADMIN_CAPTCHA + admin.getEmail());

        if (captcha == null || !captcha.equals(captchaGet)) {
            return false;
        }

        // 用户名和密码校验
        if (!AccountUtil.checkName(admin.getName())) {
            log.error("用户名格式错误");
            throw new AccountNameErrorException();
        } else if (!AccountUtil.checkPassword(admin.getPassword())) {
            log.info("密码格式错误");
            throw new AccountPasswordErrorException();
        }

        // 检验是否存在已有注册过的用户名，手机，邮箱
        if (!jsqlClient.createQuery(table)
                .where(
                        table.name().eq(admin.getName())
                ).select(
                        table.name()
                ).execute().isEmpty()) {
            log.info("用户名已存在");
            throw new AccountExistsException(AccountConstant.ACCOUNT_NAME_EXISTS);
        }

        if (!jsqlClient.createQuery(table)
                .where(
                        table.email().eq(admin.getEmail())
                ).select(table).execute().isEmpty()) {
            log.info("邮箱已存在");
            throw new AccountExistsException(AccountConstant.ACCOUNT_EMAIL_EXISTS);
        }

        if (!jsqlClient.createQuery(table)
                .where(
                        table.phone().eq(admin.getPhone())
                ).select(table).execute().isEmpty()) {
            log.info("手机号已存在");
            throw new AccountExistsException(AccountConstant.ACCOUNT_PHONE_EXISTS);
        }

        admin.setPassword(SHA256Encryption.getSHA256(admin.getPassword()));

        SimpleSaveResult<Admin> result = jsqlClient.save(admin);
        log.info("admin registered: {}", result.getModifiedEntity().id());

        return true;
    }

    @Override
    public Admin adminLogin(AdminForLogin admin) {

        // 密码格式校验
        if (!AccountUtil.checkPassword(admin.getPassword())) {
            log.info("密码格式错误");
            throw new AccountPasswordErrorException();
        }

        if (!AccountUtil.checkEmail(admin.getEmail())) {
            log.info("邮箱格式错误");
            throw new AccountNameErrorException();
        }
        List<Admin> adminList = jsqlClient.createQuery(table)
                .where(
                        table.email().eq(admin.getEmail()),
                        table.password().eq(SHA256Encryption.getSHA256(admin.getPassword()))
                ).select(table.fetch(
                        Fetchers.ADMIN_FETCHER
                                .email(true)
                                .phone(true)
                                .address(true)
//                                .responsibilityArea(true)
                                .name(true)
                                .gender(true)
                                .status(true)
                                .profileUrl(true)
                )).execute();
        if (adminList.isEmpty()) {
            log.info("用户名或密码错误");
            throw new AccountPasswordErrorException();
        }

        return adminList.getFirst();
    }

    @Override
    public Boolean adminUpdate(AdminForUpdate admin) {

        if (!AccountUtil.checkName(admin.getName())) {
            log.info("用户名格式错误");
            throw new AccountNameErrorException();
        }

        if (admin.getId() != THREAD_LOCAL_ADMIN_ID.get()) {
            log.info("修改权限不足");
            throw new AccountPermissionException();
        }

        jsqlClient.update(admin);

        return true;
    }


    /**
     * 邮箱验证码发送与验证
     *
     * @param adminForVerify 验证用户信息
     */
    // TODO 手机验证
    public void adminVerify(AdminForVerify adminForVerify) {

        // 生成验证码并设置过期时间
        String captcha = CaptchaGenerator.generateCaptcha();
        this.stringRedisTemplate.opsForValue().set(RedisConstant.USER_CAPTCHA + adminForVerify.getEmail(), captcha, 20L, TimeUnit.MINUTES);
        // 发送验证码到邮箱
        this.mailUtil.sendCaptcha(captcha, adminForVerify.getEmail());

        // 记录日志
        log.info("send captcha to: {}, captcha: {}", adminForVerify.getEmail(), captcha);
    }
}
