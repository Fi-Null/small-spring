package com.small.spring.beans;

import com.small.spring.School;
import com.small.spring.Student;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @ClassName AnnotationParserTest
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/22 16:57
 * @Version 1.0
 **/
public class AnnotationParserTest {

    @Test
    public void testAnnotationWithNonXml() throws Exception {

        // 1.测试全局扫包 component-scan,注解方式注入bean，不需要手动写xml
        // 2.测试@Value注解赋值
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("annotation_null.xml");
        School school = (School) applicationContext.getBean("school");
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(school.getStudent().getStuName());
        System.out.println(student.getSchool().getName());
        System.out.println(school.getPrice());
       /* Car carWithXml = (Car)applicationContext.getBean("carWithXml");
        System.out.println(carWithXml);
        Student studentWithXml = (Student) applicationContext.getBean("studentWithXml");
        System.out.println(studentWithXml);*/
    }
}
