package com.small.spring.aop;

import com.small.spring.beans.factory.BeanFactory;

/**
 * @ClassName BeanFactoryAware
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/22 23:46
 * @Version 1.0
 **/
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory) throws Exception;

}
