package com.small.spring.aop;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 9/27/19 2:22 PM
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
