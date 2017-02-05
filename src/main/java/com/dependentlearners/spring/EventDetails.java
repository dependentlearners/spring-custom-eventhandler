package com.dependentlearners.spring;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.springframework.context.ApplicationContext;


@Value
class EventDetails {
    final Event event;
    @Getter(value = AccessLevel.PRIVATE)
    final String beanName;
    final Method method;


    static Predicate<EventDetails> isMethodParametersGreaterThanOne() {
        return p -> p.method.getParameterCount() > 1;
    }

    public Object getEventImplementingObject(ApplicationContext applicationContext) {
        Preconditions.checkNotNull(applicationContext, "application context should not be null");
        final Object bean = applicationContext.getBean(this.beanName);
        Preconditions.checkNotNull(bean, "No bean registered in the container");
        return bean;
    }

    @Value
    static class Event {
        private final String value;

        static Event from(String value) {
            return new Event(value);
        }
    }

}
