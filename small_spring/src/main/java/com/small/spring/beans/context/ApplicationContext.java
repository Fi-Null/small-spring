package com.small.spring.beans.context;

import com.small.spring.beans.factory.AbstractBeanFactory;
import com.small.spring.beans.factory.BeanFactory;

/**
 * @InterfaceName ApplicationContext
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 22:52
 * @Version 1.0
 **/
public interface ApplicationContext extends BeanFactory {
    void setParent (ApplicationContext parent);
    ApplicationContext getParent();
    AbstractBeanFactory getBeanFactory();
}
