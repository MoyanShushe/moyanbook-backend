//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.moyanshushe.utils;

public class UserThreadLocalUtil {
    public static final ThreadLocal<Integer> THREAD_LOCAL_USER_ID = ThreadLocal.withInitial(() -> {
        return null;
    });

    private UserThreadLocalUtil() {
    }
}
