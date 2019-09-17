package com.small.spring.beans.xml;

import com.small.spring.beans.AbstractBeanDefinitionReader;
import com.small.spring.beans.io.ResourceLoader;

import java.io.InputStream;

/**
 * @ClassName XmlBeanDefinitionReader
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/17 23:43
 * @Version 1.0
 **/
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    protected XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public void loadBeanDefinitions(String location) throws Exception {
        InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
        doLoadBeanDefinitions(inputStream);
    }

    private void doLoadBeanDefinitions(InputStream inputStream) {

    }
}
