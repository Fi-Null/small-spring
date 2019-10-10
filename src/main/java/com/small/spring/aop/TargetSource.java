package com.small.spring.aop;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 9/27/19 2:28 PM
 */
public class TargetSource {
    private Class<?> targetClass;
    private Class<?>[] interfaces;

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    private Object target;

    public TargetSource() {
    }

    public TargetSource(Object target, Class<?> targetClass, Class<?>... interfaces) {
        this.targetClass = targetClass;
        this.interfaces = interfaces;
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

}
