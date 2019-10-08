package com.small.spring.beans.factory;

import com.small.spring.aop.BeanFactoryAware;
import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanReference;
import com.small.spring.beans.PropertyValue;
import com.small.spring.beans.annotation.AutoWired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName AutowireCapableBeanFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 00:07
 * @Version 1.0
 **/
public class AutowireCapableBeanFactory extends AbstractBeanFactory {


    protected void injectAnnotation(Object bean, BeanDefinition beanDefinition) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            AutoWired autowired = field.getAnnotation(AutoWired.class);
            if (autowired == null) {
                continue;
            }
            String refName = autowired.getId();
            if (refName.equals("")) {
                refName = field.getName();
            }
            secondCache.put(refName, bean);
            field.setAccessible(true);
            field.set(bean, getBean(refName));
        }
    }

    protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
        //为什么要有BeanFactoryAware?这个接口用于标注这个类是用于AOP处理的，
        // setBeanFactory是为了后面获取所有的切面类。
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
            Object value = propertyValue.getValue();
            Object convertedValue = null;

            //如果是ref，就创建这个ref
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
                String refName = beanReference.getName();

                //说明当前是循环依赖状态
                if (thirdCache.containsKey(refName) && !firstCache.containsKey(refName)) {//说明当前是循环依赖状态
                    //标注a ref b,b ref a中，b是后被循环引用的
                    secondCache.put(beanReference.getName(), bean);//标注a ref b,b ref a中，b是后被循环引用的
                }
                convertedValue = value;
            }
            //非ref字段，对value进行处理，将string转化成对应类型
            else {
                // 获得name对应的字段
                Field field = bean.getClass().getDeclaredField(propertyValue.getName());
                if (field.getType().toString().equals("class java.lang.String")) {
                    convertedValue = value;
                } else {
                    convertedValue = this.converterFactory.getConverterMap()
                            .get(field.getType())
                            .parse((String) value);
                }
            }

            //SetXXX
            try {
                Method declaredMethod = bean.getClass().getDeclaredMethod("set" + propertyValue.getName().substring(0, 1).toUpperCase()
                        + propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(bean, convertedValue);
            } catch (NoSuchMethodException e) {
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, convertedValue);
            }
        }
    }
}
