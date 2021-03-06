package com.dependentlearners.spring;

import com.google.common.collect.Lists;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class BeanProxyCreator extends AbstractAutoProxyCreator {

    @Autowired
    private EventHandlerMapping eventMapping;

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return Lists.newArrayList(beanClass.getMethods())
                .stream()
                .filter((method) -> method.isAnnotationPresent(EventListener.class))
                .findFirst()
                .map((method) -> createProxy(beanName, method))
                .orElse(null);

    }

    private Object[] createProxy(String beanName, Method method) {
        return new Object[]{new EventListenerMethodInterceptor(method)};
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
