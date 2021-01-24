package com.rj93.medium.redditoauth2.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class HelloController {

    private final WebClient webClient;

    public HelloController(@Qualifier("redditWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping
    public Mono<String> hello() {
        return getUsername()
                .map(name -> "Hello, " + name);
    }

    private Mono<String> getUsername() {
        return webClient.get()
                .uri("/api/v1/me")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(responseBody -> "u/" + responseBody.get("name"));
    }
}
