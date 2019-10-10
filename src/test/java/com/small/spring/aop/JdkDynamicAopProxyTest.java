package com.small.spring.aop;

import com.small.spring.Car;
import com.small.spring.Driveable;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 5:01 PM
 */
public class JdkDynamicAopProxyTest {

    @Test
    public void testInterceptorXml() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop.xml");
        Driveable car = (Driveable) applicationContext.getBean("car");
        car.running();
    }

    @Test
    public void testInterceptor() throws Exception {
        // --------- car without AOP
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop_noXmlConfig.xml");
        Driveable car = (Driveable) applicationContext.getBean("car");
        car.running();

        // --------- car with AOP
        // 1. 设置被代理对象(Joinpoint)
        AdvisedSupport advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(car, Car.class, Driveable.class);
        advisedSupport.setTargetSource(targetSource);

        // 2. 设置拦截器(Advice)
        TimerInterceptor timerInterceptor = new TimerInterceptor();
        advisedSupport.setMethodInterceptor(timerInterceptor);

        // 3. 创建代理(Proxy)
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
        Driveable carProxy = (Driveable) jdkDynamicAopProxy.getProxy();


        // 4. 基于AOP的调用
        carProxy.running();

    }

}
