package com.moyanshushe.exception.common;

import com.moyanshushe.constant.VerifyConstant;
import com.moyanshushe.exception.BaseException;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public class CaptchaErrorException extends BaseException {
    public CaptchaErrorException()
    {
        super(VerifyConstant.CAPTCHA_ERROR);
    }

    public CaptchaErrorException(String message)
    {
        super(message);
    }
}
