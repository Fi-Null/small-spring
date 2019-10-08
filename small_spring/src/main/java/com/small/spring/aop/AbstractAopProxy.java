package com.small.spring.aop;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 9/27/19 2:30 PM
 */
public abstract class AbstractAopProxy implements AopProxy {
    protected AdvisedSupport advised;

    public AbstractAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
}