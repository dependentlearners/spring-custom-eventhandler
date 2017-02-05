package com.dependentlearners.spring;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dependentlearners.spring.EventDetails.Event.from;
import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * @author Naresh Reddy
 *         Maps events to handlers.
 */
@Component
public class EventHandlerMapping implements ApplicationListener, BeanPostProcessor {

    private final ApplicationContext applicationContext;
    private Map<String, EventDetails> eventToBeanNameMapping = new HashMap<>();

    @Autowired
    public EventHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    /**
     * @param eventName    Name of the event for which the listener is getting registered with
     * @param eventDetails Name of the spring bean where the listener is available
     *                     <p>
     *                     Works only during application startup after that dont try to add any event it will fails as the map is changed to Immutable
     *                     </p>
     */
    private void addMapping(String eventName, EventDetails eventDetails) {
        this.eventToBeanNameMapping.put(eventName, eventDetails);
    }

    Optional<EventDetails> getEventDetails(String eventName) {
        return Optional.ofNullable(this.eventToBeanNameMapping.get(eventName));
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.eventToBeanNameMapping = ImmutableMap.copyOf(this.eventToBeanNameMapping);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Arrays.stream(getTargetClass(bean).getMethods())
                .filter(method -> method.isAnnotationPresent(EventListener.class))
                .forEach((method -> register(method, beanName)));

        return bean;
    }

    private void register(Method method, String beanName) {
        final String eventName = method.getAnnotation(EventListener.class).event();

        Stream.of(eventToBeanNameMapping.containsKey(eventName))
                .filter(val -> val)
                .forEach(val -> throwEventAlreadyRegisteredException(eventName));

        this.eventToBeanNameMapping.put(eventName, new EventDetails(from(eventName), beanName, method));
    }

    private void throwEventAlreadyRegisteredException(String eventName) {
        final EventDetails eventDetails = eventToBeanNameMapping.get(eventName);
        throw new IllegalArgumentException(String.format("Already handler is registered for event '%s' with bean name '%s' and bean type '%s' on method '%s'",
                eventName,
                eventDetails.getEvent(),
                getTargetClass(eventDetails.getEventImplementingObject(applicationContext)),
                eventDetails.getMethod().getName()));
    }
}
