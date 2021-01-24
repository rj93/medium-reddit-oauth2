package com.rj93.medium.redditoauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class RedditConfig {
    private static final String REDDIT_OAUTH2_REGISTRATION_ID = "reddit";
    private static final String REDDIT_BASE_URL = "https://oauth.reddit.com";

    @Bean(name = "redditWebClient")
    public WebClient redditWebClient(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
        oauth2.setDefaultClientRegistrationId(REDDIT_OAUTH2_REGISTRATION_ID);

        return WebClient.builder()
                .baseUrl(REDDIT_BASE_URL)
                .filter(oauth2)
                .filter(ExchangeFilterFunction.ofRequestProcessor(this::injectHeader))
                .build();
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    public Mono<ClientRequest> injectHeader(final ClientRequest clientRequest) {
        return Mono.just(ClientRequest.from(clientRequest)
                .header(HttpHeaders.USER_AGENT, "java:com.rj93.medium:v0.0.1-SNAPSHOT (by /u/-rj93)")
                .build());
    }
}
