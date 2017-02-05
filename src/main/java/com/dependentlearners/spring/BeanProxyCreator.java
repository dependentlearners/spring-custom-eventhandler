package com.dependentlearners.spring;

import com.google.common.collect.Lists;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.dependentlearners.spring.EventDetails.Event.from;

@Component
public class BeanProxyCreator extends AbstractAutoProxyCreator {

    @Autowired
    private EventMapping eventMapping;

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return Lists.newArrayList(beanClass.getMethods())
                .stream()
                .filter((method) -> method.getAnnotation(EventListener.class) != null)
                .findFirst()
                .map((method) -> createProxy(beanName, method))
                .orElse(null);

    }

    private Object[] createProxy(String beanName, Method method) {
        final String eventName = method.getAnnotation(EventListener.class).value();
        this.eventMapping.addMapping(eventName, new EventDetails(from(eventName), beanName, method));
        return new Object[]{new EventListenerMethodInterceptor(method)};
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
