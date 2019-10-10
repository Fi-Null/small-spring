package com.small.spring.beans.annotation;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.PropertyValue;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AnnotationParser
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/22 18:13
 * @Version 1.0
 **/
public class AnnotationParser {
    private Map<String, BeanDefinition> registry = new HashMap<>();

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public void setRegistry(Map<String, BeanDefinition> registry) {
        this.registry = registry;
    }

    public void annotationBeanDefinitionReader(String packageName) throws ClassNotFoundException {
        Set<String> classNames = getClassName(packageName, true);
        Class[] types = {Component.class, Service.class, Repository.class, Controller.class};

        for (String className : classNames) {
            Annotation annotation = null;
            int i = 0;

            for (i = 0; i < 4; i++) {
                annotation = Class.forName(className).getAnnotation(types[i]);
                if (annotation != null) {
                    break;
                }
            }

            if (annotation != null) {
                String beanName = null;
                switch (i) {
                    case 0:
                        beanName = ((Component) annotation).value();
                        break;
                    case 1:
                        beanName = ((Service) annotation).value();
                        break;
                    case 2:
                        beanName = ((Repository) annotation).value();
                        break;
                    case 3:
                        beanName = ((Controller) annotation).value();

                }

                if (beanName == null || beanName.length() == 0) {
                    String[] elements = className.split("\\.");
                    String sampleName = elements[elements.length - 1];
                    beanName = sampleName.substring(0, 1).toLowerCase() + sampleName.substring(1);
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setSingleton(true);
                    beanDefinition.setBeanClass(Class.forName(className));
                    processProperty(className, beanDefinition);
                    registry.put(beanName, beanDefinition);
                }
            }

        }
    }

    private void processProperty(String className, BeanDefinition beanDefinition) throws ClassNotFoundException {
        Field[] fields = Class.forName(className).getDeclaredFields();

        for (Field field : fields) {
            Value value = field.getAnnotation(Value.class);
            if (value == null) {
                continue;
            }
            String propertyValue = value.value();
            if (propertyValue != null || propertyValue.length() > 0) {
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(field.getName(), propertyValue));
            }

        }

    }

    private Set<String> getClassName(String packageName, boolean isRecursion) {
        Set<String> classNames = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = classLoader.getResource(packagePath);
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
        }

        return classNames;

    }

    private Set<String> getClassNameFromDir(String path, String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<>();
        File file = new File(path);
        File[] files = file.listFiles();

        for (File childFile : files) {
            if (childFile.isDirectory()) {
                classNames.addAll(getClassNameFromDir(childFile.getPath(), packageName + "." + childFile.getName(), isRecursion));
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    classNames.add(packageName + "." + fileName.replace(".class", ""));
                }
            }
        }

        return classNames;
    }
}