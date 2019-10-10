package com.small.spring.beans;

import com.small.spring.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AbstractBeanDefinitionReader
 * @Description 从配置中读取BeanDefinition
 * @Author xiangke
 * @Date 2019/9/17 23:38
 * @Version 1.0
 **/
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private Map<String, BeanDefinition> registry;

    private ResourceLoader resourceLoader;

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
