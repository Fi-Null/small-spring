package com.small.spring.beans;

/**
 * @ClassName PropertyValue
 * @Description 用于bean的属性注入
 * @Author xiangke
 * @Date 2019/9/17 23:40
 * @Version 1.0
 **/
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
