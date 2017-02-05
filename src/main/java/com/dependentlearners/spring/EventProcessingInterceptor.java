package com.dependentlearners.spring;

public interface EventProcessingInterceptor {
    boolean preProcessor(EventDetails eventDetails, Object eventData);

    void postProcessor(EventDetails eventDetails, Object eventData);
}
