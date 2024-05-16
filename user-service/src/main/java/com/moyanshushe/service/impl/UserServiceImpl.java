package com.moyanshushe.service.impl;

import com.moyanshushe.constant.RedisConstant;
import com.moyanshushe.mapper.UserMapper;
import com.moyanshushe.model.dto.user.*;
import com.moyanshushe.model.entity.Fetchers;
import com.moyanshushe.model.entity.User;
import com.moyanshushe.service.UserService;
import com.moyanshushe.utils.security.AccountUtil;
import com.moyanshushe.utils.security.SHA256Encryption;
import com.moyanshushe.utils.verify.CaptchaGenerator;
import com.moyanshushe.utils.verify.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.moyanshushe.utils.UserThreadLocalUtil.THREAD_LOCAL_USER_ID;


/**
 * 针对表【user】的数据库操作Service实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final MailUtil mailUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public UserServiceImpl(UserMapper userMapper, MailUtil mailUtil, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.mailUtil = mailUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 用户注册
     *
     * @param user 用户注册信息
     * @return 注册成功返回用户ID，失败返回-1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long userRegister(UserForRegister user) {
        // 校验逻辑
        // 验证码校验
        String captcha = stringRedisTemplate.opsForValue().get(RedisConstant.USER_CAPTCHA+user.getEmail());
        if (captcha == null || captcha.isEmpty() || !captcha.equals(user.getCaptcha())) {
            return -1;
        }
        // 账户名检查
        if (!AccountUtil.checkName(user.getName())) {
            return -1;
        }
        // 密码检查
        if (!AccountUtil.checkPassword(user.getPassword())) {
            return -1;
        }
        // 账户重复性检查
        long count = userMapper.findByName(user.getName(), Fetchers.USER_FETCHER).size();
        if (count > 0) {
            return -1;
        }
        // 密码加密
        user.setPassword(SHA256Encryption.getSHA256(user.getPassword()));
        // 数据库保存
        SimpleSaveResult<User> result = userMapper.addUser(user);
        if (result.getAffectedRowCount(User.class) != 1) {
            return -1;
        }
        long id = result.getModifiedEntity().id();

        // 删除验证码
        // TODO 删除手机号验证码
        stringRedisTemplate.delete(RedisConstant.USER_CAPTCHA + user.getEmail());

        log.info("user registered: {}", id);

        return id;
    }

    /**
     * 用户登录
     *
     * @param userForLogin 用户登录信息
     * @return 登录成功返回用户ID，失败返回-1
     */
    @Override
    public long userLogin(UserForLogin userForLogin) {
        // 密码检查
        if (!AccountUtil.checkPassword(userForLogin.getPassword())) {
            return -1;
        }

        // 账户名、手机号或邮箱的合法性检查与登录尝试
        Optional<User> userOptional = Optional.empty();
        if (userForLogin.getName() != null && AccountUtil.checkName(userForLogin.getName())) {

            userOptional = userMapper.findByName(
                    userForLogin.getName(), Fetchers.USER_FETCHER.password())
                    .stream().findFirst();

        } else if (userForLogin.getPhone() != null && AccountUtil.checkPhone(userForLogin.getPhone())) {

            userOptional = userMapper.findByPhone(
                    userForLogin.getPhone(), Fetchers.USER_FETCHER.password())
                    .stream().findFirst();

        } else if (userForLogin.getEmail() != null && AccountUtil.checkEmail(userForLogin.getEmail())) {

            userOptional = userMapper.findByEmail(
                    userForLogin.getEmail(), Fetchers.USER_FETCHER.password())
                    .stream().findFirst();
        }
        if (userOptional.isEmpty()) {
            return -1;
        }

        // 验证密码
        String password = userForLogin.getPassword();
        String passwordDigested = SHA256Encryption.getSHA256(password);
        if (passwordDigested.equals(userOptional.get().password())) {

            // TODO 根据登录时间长短进行决定是否要重新验证用户有效性
            // 存入现成用户id
            THREAD_LOCAL_USER_ID.set(Long.valueOf(userOptional.get().id()));

            log.info("user login: {}", userOptional.get().id());

            // 登录成功，记录登录时间
            userMapper.updateLoginTime(userOptional.get().id(), LocalDate.now());

            return userOptional.get().id();
        }

        return -1;
    }

    /**
     * 用户信息更新
     *
     * @param userForUpdate 用户更新信息
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userUpdate(UserForUpdate userForUpdate) {
        // TODO 完善更新逻辑
        if (!AccountUtil.checkName(userForUpdate.getName())) {
            return false;
        }

        // 用户ID检查
        if (userForUpdate.getId() == 0) {
            return false;
        }

        // 获取当前登录用户ID
        Object userId = THREAD_LOCAL_USER_ID.get();
        if (userId == null || !userId.equals(userForUpdate.getId())) {
            return false;
        }

        // 更新用户信息
        SimpleSaveResult<User> result = userMapper.update(userForUpdate);

        log.info("update user: {}", userId);
        log.info("details: {} -> {}", result.getOriginalEntity().toString(), result.getModifiedEntity().toString());

        return true;
    }

    /**
     * 发送验证码进行用户验证
     *
     * @param userForVerify 需要验证的用户信息
     */
    @Override
    public void userVerify(UserForVerify userForVerify) {

        // TODO 丰富验证功能
        // 生成并设置验证码
        String captcha = CaptchaGenerator.generateCaptcha();

        // 存入redis验证码
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_CAPTCHA + userForVerify.getEmail(), captcha, 20, TimeUnit.MINUTES);

        // 发送验证码
        mailUtil.sendCaptcha(captcha, userForVerify.getEmail());
        log.info("send captcha to: {}, captcha: {}", userForVerify.getEmail(), captcha);
    }

    /**
     * 将用户与特定信息绑定。
     *
     * @param userForBinding 包含需要绑定用户信息的对象。该对象应包含用户的标识符及邮箱或者手机号码以完成绑定过程。
     * @return 返回一个布尔值，若绑定成功则为true，失败则为false。
     */
    @Override
    public boolean bind(UserForBinding userForBinding) {
        // TODO 手机验证码的获取，删除
        String captcha = stringRedisTemplate.opsForValue().get(RedisConstant.USER_CAPTCHA + userForBinding.getEmail());

        if (captcha == null || captcha.isEmpty() || !captcha.equals(userForBinding.getCaptcha())) {
            return false;
        }
        if (userForBinding.getEmail() != null && AccountUtil.checkEmail(userForBinding.getEmail())) {

            userMapper.update(userForBinding.toEntity());
            log.info("user: {} bind email: {}", userForBinding.getId(), userForBinding.getEmail());

            // 移除验证码
            stringRedisTemplate.delete(RedisConstant.USER_CAPTCHA + userForBinding.getEmail());
        }

        if (userForBinding.getPhone() != null && AccountUtil.checkPhone(userForBinding.getPhone())) {

            userMapper.update(userForBinding.toEntity());
            log.info("user: {} bind phone: {}", userForBinding.getId(), userForBinding.getPhone());

            // 删除验证码
            stringRedisTemplate.delete(RedisConstant.USER_CAPTCHA + userForBinding.getPhone());
        }

        return true;
    }
}
