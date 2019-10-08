package com.small.spring.aop;

import java.lang.reflect.Method;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 9/27/19 2:22 PM
 */
public interface MethodMatcher {

    boolean matches(Method method, Class targetClass);
}
