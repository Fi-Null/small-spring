package com.small.spring.beans;

import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 4:35 PM
 */
public class PrototypeTest {

    @Test
    public void testPrototype() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("prototype.xml");
        for (int i = 0; i < 3; i++) {
            System.out.println(applicationContext.getBean("carPrototype").hashCode());

        }
        for (int i = 0; i < 3; i++)
            System.out.println(applicationContext.getBean("carSingleton").hashCode());
    }
}
