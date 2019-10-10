package com.small.spring.beans;

/**
 * @InterfaceName BeanDefinitionReader
 * @Description 从配置中读取BeanDefinition
 * @Author xiangke
 * @Date 2019/9/17 23:48
 * @Version 1.0
 **/
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;
}
