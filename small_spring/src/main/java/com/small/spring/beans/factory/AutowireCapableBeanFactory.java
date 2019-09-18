package com.small.spring.beans.factory;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanReference;
import com.small.spring.beans.PropertyValue;

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

    protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
//        if (bean instanceof BeanFactoryAware) {
//            ((BeanFactoryAware) bean).setBeanFactory(this);
//        }

        for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
                String refName = beanReference.getName();
                if (thirdCache.containsKey(refName) && !firstCache.containsKey(refName)) {//说明当前是循环依赖状态
                    secondCache.put(beanReference.getName(), bean);//标注a ref b,b ref a中，b是后被循环引用的
                }
            }

            try {
                Method declaredMethod = bean.getClass().getDeclaredMethod(
                        "set" + propertyValue.getName().substring(0, 1).toUpperCase()
                                + propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);

                declaredMethod.invoke(bean, value);
            } catch (NoSuchMethodException e) {
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }
}
