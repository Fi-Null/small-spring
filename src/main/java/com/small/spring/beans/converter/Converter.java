package com.small.spring.beans.converter;

import java.lang.reflect.Type;

/**
 * @InterfaceName Converter
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/22 17:55
 * @Version 1.0
 **/
public interface Converter<T> {

    Type getType();

    String print(T fieldValue);

    T parse(String clientValue) throws Exception;
}
