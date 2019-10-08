package com.small.spring.beans;

import com.small.spring.Driveable;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 4:40 PM
 */
public class ConstructorTest {

    @Test
    public void testConstructor() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("smallIoc.xml");
        Driveable car = (Driveable) applicationContext.getBean("carByConstructor");
        System.out.println(car);
    }

}
