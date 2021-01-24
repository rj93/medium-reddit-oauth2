package com.rj93.medium.redditoauth2.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController("/manual")
public class HelloControllerWithReactiveOAuth2AuthorizedClientService {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;
    private final WebClient webClient = WebClient.builder().build();

    public HelloControllerWithReactiveOAuth2AuthorizedClientService(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping
    public Mono<String> hello() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication().getName())
                .flatMap(this::getAccessToken)
                .flatMap(this::getUsername)
                .map(name -> "Hello, " + name);
    }

    private Mono<String> getAccessToken(String name) {
        return authorizedClientService
                .loadAuthorizedClient("reddit", name)
                .map(client -> client.getAccessToken().getTokenValue());
    }

    private Mono<String> getUsername(String token) {
        String bearerToken = "bearer " + token;
        return webClient.get()
                .uri("https://oauth.reddit.com/api/v1/me")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(responseBody -> "u/" + responseBody.get("name"));
    }
}
