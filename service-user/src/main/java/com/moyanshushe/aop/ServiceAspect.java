package com.moyanshushe.aop;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.FieldConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.moyanshushe.utils.UserThreadLocalUtil.THREAD_LOCAL_USER_ID;

@Aspect
@Slf4j
@Component
public class ServiceAspect {

    @Before("execution(* com.moyanshushe.service.*.add*(..))")
    public void setCreateOperation (JoinPoint joinPoint) {
        // 获取参数
        Object[] args = joinPoint.getArgs();

        if (args.length < 1) {
            return;
        }

        Object arg = args[0];

        // 修改参数
        Class<?> argClass = arg.getClass();

        try {
            Method setCreatePersonId = argClass.getDeclaredMethod(FieldConstant.SET_CREATE_PERSON_ID, long.class);

            setCreatePersonId.invoke(arg, THREAD_LOCAL_USER_ID.get());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            log.info("AOP failed(set create person): {}", arg);
        }

        try {
            Method setUpdatePersonId = argClass.getDeclaredMethod(FieldConstant.SET_UPDATE_PERSON_ID, long.class);

            setUpdatePersonId.invoke(arg, THREAD_LOCAL_USER_ID.get());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.info("AOP failed(set update person): {}", arg);
        }

    }

    @Before("execution(* com.moyanshushe.service.*.update*(..))")
    public void setUpdateOperation (JoinPoint joinPoint) {
        // 获取参数
        Object[] args = joinPoint.getArgs();

        if (args.length < 1) {
            return;
        }

        Object arg = args[0];

        // 修改参数
        Class<?> argClass = arg.getClass();

        try {
            Method setUpdatePersonId = argClass.getDeclaredMethod(FieldConstant.SET_UPDATE_PERSON_ID, long.class);

            setUpdatePersonId.invoke(arg, THREAD_LOCAL_USER_ID.get());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.info("AOP failed(set update person): {}", arg);
        }
    }
}
