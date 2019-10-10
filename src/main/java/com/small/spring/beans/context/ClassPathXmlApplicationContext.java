package com.small.spring.beans.context;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.annotation.AnnotationParser;
import com.small.spring.beans.factory.AbstractBeanFactory;
import com.small.spring.beans.factory.AutowireCapableBeanFactory;
import com.small.spring.beans.lifecycle.DisposableBean;
import com.small.spring.beans.xml.XmlBeanDefinitionReader;
import com.small.spring.io.ResourceLoader;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName ClassPathXmlApplicationContext
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 23:02
 * @Version 1.0
 **/
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private String configLocation;

    public ClassPathXmlApplicationContext(ApplicationContext parent, String configLocation) throws Exception {
        this(configLocation, new AutowireCapableBeanFactory(), parent);
    }

    public ClassPathXmlApplicationContext(String configLocation) throws Exception {
        this(configLocation, new AutowireCapableBeanFactory(), null);
    }

    public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory, ApplicationContext parent) throws Exception {
        super(beanFactory);
        this.setParent(parent);
        this.configLocation = configLocation;
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
        xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);

        xmlBeanDefinitionReader.getRegistry().forEach((name, beanDefinition) -> beanFactory.registerBeanDefinition(name, beanDefinition));

        AnnotationParser annotationParser = new AnnotationParser();
        String packageName = xmlBeanDefinitionReader.getPackageName();
        if (packageName == null || packageName.length() == 0) {
            return;
        }
        annotationParser.annotationBeanDefinitionReader(packageName);

        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : annotationParser.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }

    public void close() {
        Map<String, Object> thirdCache = beanFactory.getThirdCache();
        for (Map.Entry<String, BeanDefinition> entry : beanFactory.getBeanDefinitionMap().entrySet()) {
            String beanName = entry.getKey();
            Object realClassInvokeBean = thirdCache.get(beanName);
            if (realClassInvokeBean instanceof DisposableBean) {
                ((DisposableBean) realClassInvokeBean).destroy();
            }
            try {
                Method method = realClassInvokeBean.getClass().getMethod("destroy_method", null);
                method.invoke(realClassInvokeBean, null);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public AbstractBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
