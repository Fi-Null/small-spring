package com.small.spring.beans;

import com.small.spring.Life;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 4:16 PM
 */
public class LifecycleTest {

    @Test
    public void testLifecycle() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("life.xml");
        Life life = (Life) applicationContext.getBean("life");
        System.out.println(life.toString());
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }

}
