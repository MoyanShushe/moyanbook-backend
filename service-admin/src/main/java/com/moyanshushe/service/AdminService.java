package com.moyanshushe.service;

import com.moyanshushe.model.dto.admin.AdminForLogin;
import com.moyanshushe.model.dto.admin.AdminForRegister;
import com.moyanshushe.model.dto.admin.AdminForUpdate;
import com.moyanshushe.model.dto.admin.AdminForVerify;
import com.moyanshushe.model.entity.Admin;

import javax.validation.constraints.NotNull;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface AdminService {

    @NotNull
    Boolean adminRegister(AdminForRegister adminForRegister);

    Admin adminLogin(AdminForLogin adminForLogin);

    @NotNull
    Boolean adminUpdate(AdminForUpdate adminForUpdate);

    void adminVerify(AdminForVerify adminForVerify);
}
