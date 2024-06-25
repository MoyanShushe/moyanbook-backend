//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.moyanshushe.service;

import com.moyanshushe.model.dto.user.*;
import com.moyanshushe.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional(
            rollbackFor = {Exception.class}
    )
    Boolean userRegister(UserForRegister userForRegister);

    @Transactional(
            rollbackFor = {Exception.class}
    )
    User userLogin(UserForLogin userForLogin);

    @Transactional(
            rollbackFor = {Exception.class}
    )
    boolean userUpdate(UserForUpdate userForUpdate);

    void userVerify(UserForVerify userForVerify);

    boolean bind(UserForBinding userForBinding);

    boolean userUpdatePassword(UserForUpdatePassword userForUpdatePassword);
}
