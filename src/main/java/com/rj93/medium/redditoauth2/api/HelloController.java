package com.rj93.medium.redditoauth2.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {
    @GetMapping
    public Mono<String> hello() {
        return Mono.just("Hello, Medium");
    }
}
