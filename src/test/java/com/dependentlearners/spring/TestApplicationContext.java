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

        final EventAdapter bean = applicationContext.getBean(EventAdapter.class);

        bean.process("hello", null);
        bean.process("dolo", "do it my way");

        System.in.read();
    }
}
