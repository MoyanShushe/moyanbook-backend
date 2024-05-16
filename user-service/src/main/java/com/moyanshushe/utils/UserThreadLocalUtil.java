package com.moyanshushe.utils;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public class UserThreadLocalUtil {

    public static final ThreadLocal<Long> THREAD_LOCAL_USER_ID = ThreadLocal.withInitial(() -> null);

    private UserThreadLocalUtil() {}
}
