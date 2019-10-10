package com.small.spring.beans;

/**
 * @ClassName BeanReference
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 00:12
 * @Version 1.0
 **/
public class BeanReference {
    private String name;

    private Object bean;

    public BeanReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

}
