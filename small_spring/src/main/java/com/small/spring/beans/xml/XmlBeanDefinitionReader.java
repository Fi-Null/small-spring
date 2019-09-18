package com.small.spring.beans.xml;

import com.small.spring.beans.AbstractBeanDefinitionReader;
import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanReference;
import com.small.spring.beans.PropertyValue;
import com.small.spring.beans.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @ClassName XmlBeanDefinitionReader
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/17 23:43
 * @Version 1.0
 **/
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public void loadBeanDefinitions(String location) throws Exception {
        InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
        doLoadBeanDefinitions(inputStream);
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        //parse bean
        parseAndRegisterBeanDefinitions(document);
        inputStream.close();
    }

    public void parseAndRegisterBeanDefinitions(Document document) {
        Element root = document.getDocumentElement();

        parseBeanDefinitions(root);
    }

    protected void parseBeanDefinitions(Element root) {
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node instanceof Element) {
                Element element = (Element) node;
                processBeanDefinition(element);
            }
        }

    }

    protected void processBeanDefinition(Element element) {
        String name = element.getAttribute("id");
        String className = element.getAttribute("class");

        BeanDefinition beanDefinition = new BeanDefinition();
        processProperty(element, beanDefinition);

        beanDefinition.setBeanClassName(className);
        getRegistry().put(name, beanDefinition);
    }

    protected void processProperty(Element element, BeanDefinition beanDefinition) {
        NodeList propertyNodes = element.getElementsByTagName("property");

        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Node node = propertyNodes.item(i);

            if (node instanceof  Element) {
                Element propertyElement = (Element) node;
                String name = propertyElement.getAttribute("name");
                String value = propertyElement.getAttribute("value");

                if (value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                } else {
                    String ref = propertyElement.getAttribute("ref");

                    if (ref == null || ref.length() == 0) {
                        throw new IllegalArgumentException("Configuration problem: <property> element for property '"
                                + name + "' must specify a ref or value");

                    }

                    BeanReference beanReference = new BeanReference(ref);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
                }

            }

        }

    }

}
