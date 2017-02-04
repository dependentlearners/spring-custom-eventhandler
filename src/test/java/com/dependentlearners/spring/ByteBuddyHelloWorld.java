package com.dependentlearners.spring;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.truth.Truth.assertThat;

public class ByteBuddyHelloWorld {
    private final Logger logger = LoggerFactory.getLogger(ByteBuddyHelloWorld.class);

    @Test
    public void testByteBuddyHelloWorldReplacement() throws IllegalAccessException, InstantiationException {
        final Class<?> toString = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        assertThat(toString.newInstance().toString()).contains("Hello World!");
    }

    @Test
    public void testDynamicThreshHold() {
        String name = "User2";

        if (name.equals("User2")) {
            ThreadContext.put("level", "DEBUG");
        }

        logger.debug("ABC");

        ThreadContext.remove("level");
    }
}
