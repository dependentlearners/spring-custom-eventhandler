package com.dependentlearners.spring;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class BeanCreationListener extends AbstractAutoProxyCreator {

    @Autowired
    private EventRegistry eventRegistry;

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        final Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            final EventListener annotation = method.getAnnotation(EventListener.class);
            if (annotation != null) {
                eventRegistry.addEventListener(annotation.value(), beanName, method);
                return new Object[]{new EventListenerMethodInterceptor(annotation, method)};
            }
        }
        return null;
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
