package com.small.spring.beans;

/**
 * @ClassName BeanPostProcessor
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/17 23:54
 * @Version 1.0
 **/
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception;

    Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;


}
