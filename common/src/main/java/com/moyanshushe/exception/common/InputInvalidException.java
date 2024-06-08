package com.moyanshushe.exception.common;

import com.moyanshushe.constant.CommonConstant;
import com.moyanshushe.exception.BaseException;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public class InputInvalidException extends BaseException {

    public InputInvalidException() {
        super(CommonConstant.INPUT_INVALID);
    }

    public InputInvalidException(String msg) {
        super(msg);
    }
}
