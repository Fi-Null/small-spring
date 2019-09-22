package com.small.spring.beans.xml;

import com.small.spring.beans.*;
import com.small.spring.io.ResourceLoader;
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
        registerBeanDefinition(document);
        inputStream.close();
    }

    private void registerBeanDefinition(Document doc) {
        Element root = doc.getDocumentElement();
        parseBeanDefinitions(root);
    }

    private void parseBeanDefinitions(Element root) {
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node instanceof Element) {
                Element element = (Element) node;
                String packageName = null;
                if ((packageName = element.getAttribute("base-package")) != null
                        && packageName != "") {
                    this.setPackageName(packageName);
                } else {
                    processBeanDefinition(element);
                }
            }
        }
    }

    protected void processBeanDefinition(Element element) {
        String name = element.getAttribute("id");
        String className = element.getAttribute("class");
        String scope = element.getAttribute("scope");

        BeanDefinition beanDefinition = new BeanDefinition();
        processProperty(element, beanDefinition);
        processConstructorArgument(element, beanDefinition);

        beanDefinition.setBeanClassName(className);
        getRegistry().put(name, beanDefinition);
    }

    private void processConstructorArgument(Element element, BeanDefinition beanDefinition) {
        NodeList constructorNodes = element.getElementsByTagName("constructor-arg");
        for (int i = 0; i < constructorNodes.getLength(); i++) {
            Node node = constructorNodes.item(i);
            if (node instanceof Element) {
                Element constructorElement = (Element) node;
                String name = constructorElement.getAttribute("name");
                String type = constructorElement.getAttribute("type");
                String value = constructorElement.getAttribute("value");
                if (value != null && value.length() > 0) {//有value标签
                    beanDefinition.getConstructorArgument().addArgumentValue(new ConstructorArgument.ValueHolder(value, type, name));
                } else {
                    String ref = constructorElement.getAttribute("ref");
                    if (ref == null || ref.length() == 0) {
                        throw new IllegalArgumentException("Configuration problem: <constructor-arg> element for property '"
                                + name + "' must specify a ref or value");
                    }
                    BeanReference beanReference = new BeanReference(ref);
                    beanDefinition.getConstructorArgument().addArgumentValue(new ConstructorArgument.ValueHolder(beanReference, type, name));
                }
            }
        }
    }

    protected void processProperty(Element element, BeanDefinition beanDefinition) {
        NodeList propertyNodes = element.getElementsByTagName("property");

        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Node node = propertyNodes.item(i);

            if (node instanceof Element) {
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
