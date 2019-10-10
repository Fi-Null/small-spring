package com.small.spring.beans.factory;

/**
 * @InterfaceName BeanFactory
 * @Description bean的容器
 * @Author xiangke
 * @Date 2019/9/17 23:52
 * @Version 1.0
 **/
public interface BeanFactory {
    Object getBean(String name) throws Exception;
}
