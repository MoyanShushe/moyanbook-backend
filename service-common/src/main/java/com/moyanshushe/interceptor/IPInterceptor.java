package com.moyanshushe.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/*
 * Author: Hacoj
 * Version: 1.0
 */

@Slf4j
@Component
public class IPInterceptor implements HandlerInterceptor {
    private final List<String> allowedIps;

    public IPInterceptor() {
        // 从配置中获取IP白名单
        allowedIps = Arrays.asList("192.168.1.1", "192.168.1.2");
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        String clientIp = request.getRemoteAddr();
        return allowedIps.contains(clientIp);
    }
}
