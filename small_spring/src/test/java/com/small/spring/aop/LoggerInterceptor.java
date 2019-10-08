package com.small.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 5:20 PM
 */
public class LoggerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("logger start!");
        Object proceed = methodInvocation.proceed();
        System.out.println("logger end ");
        return proceed;
    }
}