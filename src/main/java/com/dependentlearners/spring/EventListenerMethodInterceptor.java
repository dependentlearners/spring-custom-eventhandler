package com.dependentlearners.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventListenerMethodInterceptor implements MethodInterceptor {
    private final Method methodEventHandler;

    EventListenerMethodInterceptor(Method methodEventHandler) {
        this.methodEventHandler = methodEventHandler;
    }

    public void proceed(Object data) throws InvocationTargetException, IllegalAccessException {
        this.methodEventHandler.invoke(this);
    }

    @Override
    public Object invoke(MethodInvocation method) throws Throwable {
        final Object proceed = method.proceed();

//TODO: Do cleaning activity

        if (this.methodEventHandler.equals(method.getMethod())) {
        }

        return proceed;
    }
}
