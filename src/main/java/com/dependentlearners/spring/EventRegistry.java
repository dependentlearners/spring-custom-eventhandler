package com.dependentlearners.spring;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class EventRegistry implements ApplicationListener {
    private final ApplicationContext applicationContext;
    /**
     * Spring application context always loads in single thread so no need to have concurrent hash map after context refresh we will make it immutable map
     */
    private Map<String, String> eventToBeanNameMapping = new HashMap<>();
    private Map<String, Method> eventToMethodNameMapping = new HashMap<>();

    @Autowired
    public EventRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @param eventName Name of the event for which the listener is getting registered with
     * @param beanName  Name of the spring bean where the listener is available
     * @param method    Method which contains the event processing
     *                  <p>
     *                  Works only during application startup after that dont try to add any event it will fails as the map is changed to Immutable
     *                  </p>
     */
    void addEventListener(String eventName, String beanName, Method method) {
        eventToBeanNameMapping.put(eventName, beanName);
        eventToMethodNameMapping.put(eventName, method);
    }

    @PostConstruct
    public void hello() {
        System.out.println("Hello");
    }

    void invoke(String eventName, Object data) {
        checkNotNull(eventName, "Event id to start the processing should not be null");

        final String beanName = eventToBeanNameMapping.get(eventName);

        checkNotNull(beanName, "No event listener is registered for the given event name");

        final Object eventListenerObject = applicationContext.getBean(beanName);

        checkNotNull(eventListenerObject, "Event listener object is not available, this should never happen something wrong");

        final Method method = eventToMethodNameMapping.get(eventName);

        checkNotNull(eventListenerObject, "Event listener method is not available, this should never happen something wrong");


        if (method.getParameterCount() > 1) {
            throw new IllegalArgumentException("Don't allow more than one argument as of now");
        }


        try {
            if (method.getParameterCount() == 1) {
                method.invoke(eventListenerObject, data);
            } else {
                method.invoke(eventListenerObject);
            }
        } catch (Exception e) {
            //TODO : clean or do something
        }

    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            eventToBeanNameMapping = ImmutableMap.copyOf(eventToBeanNameMapping);
            eventToMethodNameMapping = ImmutableMap.copyOf(eventToMethodNameMapping);
        }
    }
}
