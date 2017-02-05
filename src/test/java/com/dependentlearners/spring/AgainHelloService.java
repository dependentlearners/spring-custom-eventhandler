package com.dependentlearners.spring;

import org.springframework.stereotype.Component;

@Component
public class AgainHelloService {
    @EventListener(event = "bolo")
    public void kolo(String k) {
        System.out.println(String.format("kolo %s", k));
    }
}
