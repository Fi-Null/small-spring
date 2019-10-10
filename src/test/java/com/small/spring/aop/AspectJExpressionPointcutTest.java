package com.small.spring.aop;

import com.small.spring.Car;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 4:55 PM
 */
public class AspectJExpressionPointcutTest {

    @Test
    public void testClassFilter() throws Exception {
        String expression = "execution(* com.small.spring..*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.getClassFilter().matches(Car.class);
        System.out.println(matches);
        Assert.assertTrue(matches);
    }

    @Test
    public void testMethodInterceptor() throws Exception {
        String expression = "execution(* com.small.spring..*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.getMethodMatcher().matches(Car.class.getDeclaredMethod("running"), Car.class);
        System.out.println(matches);
        Assert.assertTrue(matches);
    }


}
