package com.small.spring.beans.factory;

import com.small.spring.beans.BeanDefinition;
import com.small.spring.beans.BeanPostProcessor;
import com.small.spring.beans.BeanReference;
import com.small.spring.beans.ConstructorArgument;
import com.small.spring.beans.context.AbstractApplicationContext;
import com.small.spring.beans.context.ApplicationContext;
import com.small.spring.beans.converter.ConverterFactory;
import com.small.spring.beans.lifecycle.InitializingBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    protected final List<String> beanDefinitionNames = new ArrayList<String>();
    //存放已经构造完整的实现了BeanPostProcessor的实例
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    /*** 通过三级缓存彻底解决了单例setter注入下的循环依赖 ***/
    /** 当执行完方法beanFactory.preInstantiateSingletons()后，thirdCache保存了所有空构造实例及名称，
     * secondCache保存了所有可能需要重新设置ref的实例及名称，first保存了所有最终生成的实例和名称。在firstcache与third
     * 中，必然存放了所有的bean，在second中只存放因循环依赖所以创建时ref了不完整对象的那些。在创建了所有实例后，
     * 通过checkoutAll方法对secondCache中的实例进行重置依赖。
     **/

    //firstCache用于保存所有最终被生成的实例.
    protected Map<String, Object> firstCache = new HashMap<>();
    //a ref b, b ref a情况下，在b创建时，a还只是空构造实例，
    // 因此用secondCache保存所有field中指向空实例的那些实例，即保存b。
    protected Map<String, Object> secondCache = new HashMap();
    //thirdCache是当空构造函数创建一个实例时，就放入其中。
    protected Map<String, Object> thirdCache = new HashMap<>();


    protected ConverterFactory converterFactory = new ConverterFactory();
    protected AbstractApplicationContext context;

    public AbstractApplicationContext getContext() {
        return context;
    }

    public void setContext(AbstractApplicationContext context) {
        this.context = context;
    }

    public Object getBean(String name) throws Exception {

        BeanDefinition beanDefinition = beanDefinitionMap.get(name);

        ApplicationContext context = this.getContext();

        while (beanDefinition == null && context.getParent() != null) {
            ApplicationContext parent = context.getParent();
            Object object = parent.getBean(name);
            if (object == null) {
                return object;
            } else {
                context = parent;
            }
        }

        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is defined");
        }
        Object bean = beanDefinition.getBean();

        //如果bean==null说明还未存在，不是单例说明是否存在都要重新创建
        if (bean == null) {
            //根据生命周期来的，先创建后进行before，init,after
            bean = doCreateBean(name, beanDefinition);
            bean = initializeBean(bean, name);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    protected Object initializeBean(Object bean, final String name) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }

        try {
            Method method = bean.getClass().getMethod("init_method", null);
            method.invoke(bean, null);
        } catch (Throwable t) {

        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
        }

        //空构造实例如果被AOP成代理实例，则放入一级缓存，说明已经构建完毕
        if (thirdCache.containsKey(name)) {
            firstCache.put(name, bean);
        }

        return bean;
    }

    protected Object doCreateBean(String name, BeanDefinition beanDefinition) throws Exception {
        // 创建出的可能是空也可能是有参构造函数，但是均不是构造完全的
        Object bean = createBeanInstance(beanDefinition);
        thirdCache.put(name, bean);//thirdCache中放置的全是空构造方法构造出的实例
        beanDefinition.setBean(bean);
        applyPropertyValues(bean, beanDefinition);
        injectAnnotation(bean, beanDefinition);
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        return bean;
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
    }

    protected void injectAnnotation(Object bean, BeanDefinition beanDefinition) throws Exception {
    }


    //增加构造函数版本1.0，只判断参数数量相同
    protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        if (beanDefinition.getConstructorArgument().isEmpty()) {
            return beanDefinition.getBeanClass().newInstance();
        } else {
            List<ConstructorArgument.ValueHolder> valueHolders = beanDefinition.getConstructorArgument().getArgumentValues();
            Class clazz = Class.forName(beanDefinition.getBeanClassName());
            Constructor[] cons = clazz.getConstructors();
            for (Constructor constructor : cons) {
                if (constructor.getParameterCount() == valueHolders.size()) {
                    Object[] params = new Object[valueHolders.size()];
                    for (int i = 0; i < params.length; i++) {
                        params[i] = valueHolders.get(i).getValue();
                        if (params[i] instanceof BeanReference) {
                            BeanReference ref = (BeanReference) params[i];
                            String refName = ref.getName();
                            if (thirdCache.containsKey(refName) && !firstCache.containsKey(refName)) {
                                throw new IllegalAccessException("构造函数循环依赖" + refName);
                            } else {
                                params[i] = getBean(refName);
                            }
                        }
                    }
                    return constructor.newInstance(params);
                }
            }
        }
        return null;
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    //预先单例的初始化所有bean
    public void preInstantiateSingletons() throws Exception {
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    //返回所有实现beanPostProcessor接口的实例，这些实例都是已经创建完毕的
    public List getBeansForType(Class type) throws Exception {
        List beans = new ArrayList<Object>();

        for (String beanDefinitionName : beanDefinitionNames) {
            //a.isAssignableForm(b):a是否是b的同类或父类，类似b instanceof a。
            // 但是isAssignableFrom是类与类的关系，instanceof是实例与实例的
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

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public void setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }
}
