package com.small.spring.beans.context;

import com.small.spring.beans.factory.AbstractBeanFactory;
import com.small.spring.beans.factory.AutowireCapableBeanFactory;
import com.small.spring.beans.io.ResourceLoader;
import com.small.spring.beans.xml.XmlBeanDefinitionReader;

/**
 * @ClassName ClassPathXmlApplicationContext
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/18 23:02
 * @Version 1.0
 **/
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private String configLocation;

    public ClassPathXmlApplicationContext(String configLocation) throws Exception {
        this(configLocation, new AutowireCapableBeanFactory());
    }

    public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
        super(beanFactory);
        this.configLocation = configLocation;
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
        xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);

        xmlBeanDefinitionReader.getRegistry().forEach((name, beanDefinition) -> beanFactory.registerBeanDefinition(name, beanDefinition));
    }
}
