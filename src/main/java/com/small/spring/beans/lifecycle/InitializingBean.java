package com.small.spring.beans.lifecycle;

/**
 * @InterfaceName InitializingBean
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/22 22:50
 * @Version 1.0
 **/
public interface InitializingBean {
    void afterPropertiesSet();
}
