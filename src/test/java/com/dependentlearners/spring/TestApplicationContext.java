package com.dependentlearners.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Configuration
@ComponentScan("com.dependentlearners.spring")
public class TestApplicationContext {

    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        final AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(TestApplicationContext.class);

        final EventRegistry bean = applicationContext.getBean(EventRegistry.class);
        bean.invoke("hello", null);
        bean.invoke("bolo", "do it my way");
    }
}
