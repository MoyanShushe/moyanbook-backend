package com.moyanshushe.aop;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.FieldConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
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
        // TODO Auto-generated method stub
        Class<?> argClass = arg.getClass();

        try {
            Method setCreateTime = argClass.getDeclaredMethod(FieldConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime = argClass.getDeclaredMethod(FieldConstant.SET_UPDATE_TIME, LocalDateTime.class);

            setUpdateTime.invoke(arg, LocalDate.now());
            setCreateTime.invoke(arg, LocalDate.now());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {

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
        // TODO Auto-generated method stub , create person id is need to be generated
        Class<?> argClass = arg.getClass();

        try {
            Method setUpdateTime = argClass.getDeclaredMethod(FieldConstant.SET_UPDATE_TIME, LocalDateTime.class);

            setUpdateTime.invoke(arg, LocalDateTime.now());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
    }
}
