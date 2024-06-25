package com.moyanshushe.exception.work;

import com.moyanshushe.constant.WorkConstant;
import com.moyanshushe.exception.BaseException;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public class ConfirmCodeInvaildException extends BaseException {

    public ConfirmCodeInvaildException() {
        super(WorkConstant.ADMIN_CONFIRM_CODE_INVALID);
    }
    public ConfirmCodeInvaildException(String message) {
        super(message);
    }
}
