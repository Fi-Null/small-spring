package com.small.spring.aop;

import com.small.spring.Address;
import com.small.spring.Car;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 5:32 PM
 */
public class Cglib2AopProxyTest {


    // 只有在开启全局Cglib2AopProxy时，该case才能运行成功
    @Test
    public void testCglib() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop.xml");
        Car car = (Car) applicationContext.getBean("car");
        car.getAddress().living();
        Address address = (Address) applicationContext.getBean("address");
        address.getCar().running();
    }

}
