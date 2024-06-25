package com.moyanshushe.interceptor;

import com.moyanshushe.properties.CaptchaProperty;
import com.moyanshushe.utils.security.JwtUtil;
import com.moyanshushe.utils.security.SHA256Encryption;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*
 * Author: Hacoj
 * Version: 1.0
 */

@Slf4j
@Component
public class VerifyInterceptor implements HandlerInterceptor {

    public final CaptchaProperty captchaProperty;

    public final String captcha;

    public VerifyInterceptor(CaptchaProperty captchaProperty) {
        this.captchaProperty = captchaProperty;
        this.captcha = SHA256Encryption.getSHA256(captchaProperty.getKey());
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        String token = request.getHeader(captchaProperty.getKey());

        if (token == null) {
            log.warn("未认证的访问");
            return false;
        }

        Claims claims = JwtUtil.parseJWT(token, captchaProperty.getKey());
        Object result = claims.get(captchaProperty.getSecret());

        if (result.equals(captchaProperty.getSecret())) {
            return true;
        }

        return false;
    }
}
