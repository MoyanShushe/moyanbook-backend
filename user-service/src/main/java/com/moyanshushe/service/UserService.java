package com.moyanshushe.service;


import com.moyanshushe.model.dto.user.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hacoj
 *
 * 用户service层
 */
public interface UserService {
    /**
     * 用户注册函数
     *
     * @param userForRegister 包含注册用户信息的对象，例如用户名、密码等。
     * @return long 注册成功的用户ID。
     */
    @Transactional(rollbackFor = Exception.class)
    long userRegister(UserForRegister userForRegister);

    /**
     * 处理用户登录请求。
     *
     * @param userForLogin 包含登录所需用户信息的对象，例如用户名和密码。
     * @return long 登录成功返回用户的ID，失败返回-1。
     */
    @Transactional(rollbackFor = Exception.class)
    long userLogin(UserForLogin userForLogin);


    /**
     * 更新用户信息。
     *
     * @param userForUpdate 包含需要更新的用户信息的对象。
     * @return boolean 返回一个布尔值，表示更新操作是否成功。
     */
    @Transactional(rollbackFor = Exception.class)
    boolean userUpdate(UserForUpdate userForUpdate);

    /**
     * 校验用户信息的函数。
     * 该方法接收一个UserForVerify对象，用于验证用户的合法性。
     * 验证过程中记录日志。
     *
     * @param userForVerify 包含待验证用户信息的对象。
     */
    void userVerify(UserForVerify userForVerify);


    /**
     * 将用户与特定信息绑定。
     *
     * @param userForBinding 包含需要绑定用户信息的对象。该对象应包含用户的标识符及其他必要信息以完成绑定过程。
     * @return 返回一个布尔值，若绑定成功则为true，失败则为false。
     */
    boolean bind(UserForBinding userForBinding);
}
