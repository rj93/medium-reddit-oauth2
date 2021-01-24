# Spring Boot Reddit OAuth 2.0 Example
An exmaple Spring Boot application that uses Reddit's OAuth 2.0 for authentication, and invokes a downstream OAuth protected resource.

An accompanying Medium post can be found [here](https://rj93.medium.com/spring-webflux-and-oauth-2-0-47e0c32c0a7a).

## Prerequisites
* Java 11

## Required confgiuration
You are required to update the `clientId` and `clientSecret` values in `./src/main/resources/application.yml` for the service to work correcttly.

## Running
```bash
./mvnw spring-boot:run
```
