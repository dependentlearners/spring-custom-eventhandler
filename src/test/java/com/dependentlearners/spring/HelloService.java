package com.dependentlearners.spring;

import org.springframework.stereotype.Component;

@Component
public class HelloService {

    @EventListener(event = "hello")
    public void printHello() {
        System.out.println("hello");
    }

    @EventListener(event = "dolo")
    public void dolo() {
        System.out.println("dolo");
    }
}
