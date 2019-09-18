package com.small.spring.beans.factory;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanPostProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName AbstractBeanFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/17 23:53
 * @Version 1.0
 **/
public abstract class AbstractBeanFactory implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private final List<String> beanDefinitionNames = new ArrayList<String>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();


    protected Map<String,Object> secondCache = new HashMap();
    protected Map<String,Object> thirdCache = new HashMap<>();
    protected Map<String,Object> firstCache = new HashMap<>();

    public Object getBean(String name) throws Exception {

        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is defined");
        }
        Object bean = beanDefinition.getBean();
        if (bean == null) {
            bean = doCreateBean(name, beanDefinition);
            bean = initializeBean(bean, name);

            if(thirdCache.containsKey(name)){//空构造实例如果被AOP成代理实例，则放入三级缓存，说明已经构建完毕
                firstCache.put(name,bean);
            }

            beanDefinition.setBean(bean);
        }
        return bean;
    }

    protected Object initializeBean(Object bean, final String name) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }

        // TODO:call initialize method
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
        }

        return bean;
    }

    protected Object doCreateBean(String name, BeanDefinition beanDefinition) throws Exception {
        Object bean = createBeanInstance(beanDefinition);
        thirdCache.put(name, bean);//thirdCache中放置的全是空构造方法构造出的实例
        beanDefinition.setBean(bean);
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        return beanDefinition.getBeanClass().newInstance();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    public void preInstantiateSingletons() throws Exception {
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List getBeansForType(Class type) throws Exception {
        List beans = new ArrayList<Object>();

        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }
        return beans;
    }

    public Map<String, Object> getSecondCache() {
        return secondCache;
    }

    public void setSecondCache(Map<String, Object> secondCache) {
        this.secondCache = secondCache;
    }

    public Map<String, Object> getThirdCache() {
        return thirdCache;
    }

    public void setThirdCache(Map<String, Object> thirdCache) {
        this.thirdCache = thirdCache;
    }

    public Map<String, Object> getFirstCache() {
        return firstCache;
    }

    public void setFirstCache(Map<String, Object> firstCache) {
        this.firstCache = firstCache;
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public void setBeanDefinitionMap(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
    }
}
