package com.dependentlearners.spring;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(value = RUNTIME) //Used at runtime via reflection to load all the serviec marked with Event listener
@Target(ElementType.METHOD)
public @interface EventListener {
    AckMode ackMode() default AckMode.AUTO;

    String event();
}
