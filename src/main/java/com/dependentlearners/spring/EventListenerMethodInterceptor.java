package com.dependentlearners.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventListenerMethodInterceptor implements MethodInterceptor {
    private final EventListener eventListener;
    private final Method methodEventHandler;

    EventListenerMethodInterceptor(EventListener eventListener, Method methodEventHandler) {
        this.eventListener = eventListener;
        this.methodEventHandler = methodEventHandler;
    }

    public void proceed(Object data) throws InvocationTargetException, IllegalAccessException {
        methodEventHandler.invoke(this);
    }

    @Override
    public Object invoke(MethodInvocation method) throws Throwable {
        final Object proceed = method.proceed();

        //TODO: Do cleaning activity
        if (methodEventHandler.equals(method.getMethod())) {

        }

        return proceed;
    }
}
