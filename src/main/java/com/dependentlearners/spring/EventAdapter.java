package com.dependentlearners.spring;

import java.lang.reflect.InvocationTargetException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import static com.dependentlearners.spring.EventDetails.isMethodParametersGreaterThanOne;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
public class EventAdapter {
    final EventMapping eventMapping;
    final ApplicationContext applicationContext;

    @Autowired
    public EventAdapter(EventMapping eventMapping, ApplicationContext applicationContext) {
        this.eventMapping = eventMapping;
        this.applicationContext = applicationContext;
    }

    void process(final String eventName, final Object data) {
        checkNotNull(eventName, "Event name should not be empty");
        this.eventMapping.getEventDetails(eventName)
                .<Runnable>map(eventDetails -> () -> invokeEventProcessor(data, eventDetails))
                .orElse(() -> ignoreEvent(eventName))
                .run();
    }

    private void ignoreEvent(String eventName) {
        log.info("Unable to find the event processor for event {} ignoring the event no special action is taken", eventName);
    }

    private void invokeEventProcessor(Object data, EventDetails eventDetails) {
        final Object targetObject = eventDetails.getEventImplementingObject(this.applicationContext);

        checkArgument(isMethodParametersGreaterThanOne().negate().test(eventDetails),
                "Event invocation Don't support more than one argument");

        try {
            if (eventDetails.getMethod().getParameterCount() == 1)
                invokeWithParameters(data, eventDetails, targetObject);
            else invokeWithOutParameters(eventDetails, targetObject);

        } catch (Exception e) {
            //TODO : clean or do something
        }
    }

    private void invokeWithOutParameters(EventDetails eventDetails, Object targetObject) throws IllegalAccessException, InvocationTargetException {
        eventDetails.getMethod().invoke(targetObject);
    }

    private void invokeWithParameters(Object data, EventDetails eventDetails, Object targetObject) throws IllegalAccessException, InvocationTargetException {
        eventDetails.getMethod().invoke(targetObject, data);
    }
}
