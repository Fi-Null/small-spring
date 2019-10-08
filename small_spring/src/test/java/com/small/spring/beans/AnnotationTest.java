package com.small.spring.beans;

import com.small.spring.School;
import com.small.spring.Student;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @ClassName AnnotationTest
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/24 23:21
 * @Version 1.0
 **/
public class AnnotationTest {

    @Test
    public void testAnnotation() throws Exception {
        // 1.在xml中配置实例及属性
        // 2.在类中用@Autowired注解标注依赖的bean
        // 3.依赖和被依赖的实例都要在xml中配置
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("annotation.xml");
        School school = (School) applicationContext.getBean("school");
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(school.getStudent().getStuName());
        System.out.println(student.getSchool().getPrice());
    }
}
