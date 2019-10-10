package com.small.spring.beans.context;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanPostProcessor;
import com.small.spring.beans.PropertyValue;
import com.small.spring.beans.factory.AbstractBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * https://blog.csdn.net/w8253497062015/article/details/90274387
 *
 * @ClassName AbstractApplicationContext
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 22:54
 * @Version 1.0
 **/
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected AbstractBeanFactory beanFactory;

    private ApplicationContext parent;

    @Override
    public ApplicationContext getParent() {
        return parent;
    }

    @Override
    public void setParent(ApplicationContext parent) {
        this.parent = parent;
    }

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void refresh() throws Exception {
        beanFactory.setContext(this);
        loadBeanDefinitions(beanFactory);
        registerBeanPostProcessors(beanFactory);

        onRefresh();
    }

    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);

        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
        }
    }

    protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

    protected void onRefresh() throws Exception {
        beanFactory.preInstantiateSingletons();
        checkoutAll();
    }

    private void checkoutAll() {
        Map<String, Object> secondCache = beanFactory.getSecondCache();
        Map<String, BeanDefinition> beanDefinitionMap = beanFactory.getBeanDefinitionMap();
        for (Map.Entry<String, Object> entry : secondCache.entrySet()) {
            String invokeBeanName = entry.getKey();
            BeanDefinition beanDefinition = beanDefinitionMap.get(invokeBeanName);
            try {
                resetReference(invokeBeanName, beanDefinition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetReference(String invokeBeanName, BeanDefinition beanDefinition) throws Exception {
        Map<String, Object> thirdCache = beanFactory.getThirdCache();
        Map<String, Object> firstCache = beanFactory.getFirstCache();
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
            String refName = propertyValue.getName();
            if (firstCache.containsKey(refName)) {//如果是ref，就创建这个ref
                Object exceptedValue = firstCache.get(refName);
                Object invokeBean = beanDefinition.getBean();
                Object realClassInvokeBean = thirdCache.get(invokeBeanName);
                Object realClassRefBean = thirdCache.get(refName);
                try {
                    Method declaredMethod = realClassInvokeBean.getClass().getDeclaredMethod("set" + propertyValue.getName().substring(0, 1).toUpperCase()
                            + propertyValue.getName().substring(1), realClassRefBean.getClass());
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke((realClassInvokeBean.getClass().cast(invokeBean)), (realClassRefBean.getClass().cast(exceptedValue)));
                } catch (NoSuchMethodException e) {
                    Field declaredField = realClassInvokeBean.getClass().getDeclaredField(propertyValue.getName());
                    declaredField.setAccessible(true);
                    declaredField.set((realClassInvokeBean.getClass().cast(invokeBean)), (realClassRefBean.getClass().cast(exceptedValue)));
                }
            }
        }
    }

    @Override
    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }
}
