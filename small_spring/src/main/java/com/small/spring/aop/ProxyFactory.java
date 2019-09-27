package com.small.spring.aop;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 9/27/19 2:27 PM
 */
public class ProxyFactory extends AdvisedSupport implements AopProxy {

    @Override
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    protected final AopProxy createAopProxy() {
        //如果这个类没有接口
        //return new Cglib2AopProxy(this);
        if (getTargetSource().getInterfaces() == null || getTargetSource().getInterfaces().length == 0) {
            return new Cglib2AopProxy(this);
        }
        return new JdkDynamicAopProxy(this);
    }

}
