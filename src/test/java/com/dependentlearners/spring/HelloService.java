package com.dependentlearners.spring;

import org.springframework.stereotype.Component;

@Component
public class HelloService {

    @EventListener("hello")
    public void printHello() {
        System.out.println("hello");
    }
}
