package com.dependentlearners.spring;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Naresh Reddy
 *         Maps events to handlers.
 */
@Component
public class EventMapping implements ApplicationListener {
    /**
     * Spring application context always loads in single thread so no need to have concurrent hash map after context refresh we will make it immutable map
     */
    private Map<String, EventDetails> eventToBeanNameMapping = new HashMap<>();
    private Map<String, Method> eventToMethodNameMapping = new HashMap<>();


    /**
     * @param eventName    Name of the event for which the listener is getting registered with
     * @param eventDetails Name of the spring bean where the listener is available
     *                     <p>
     *                     Works only during application startup after that dont try to add any event it will fails as the map is changed to Immutable
     *                     </p>
     */
    void addMapping(String eventName, EventDetails eventDetails) {
        this.eventToBeanNameMapping.put(eventName, eventDetails);
    }

    Optional<EventDetails> getEventDetails(String eventName) {
        return Optional.ofNullable(this.eventToBeanNameMapping.get(eventName));
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.eventToBeanNameMapping = ImmutableMap.copyOf(this.eventToBeanNameMapping);
            this.eventToMethodNameMapping = ImmutableMap.copyOf(this.eventToMethodNameMapping);
        }
    }
}
