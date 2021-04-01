package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/foo")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8002"))
                .route(p -> p
                        .path("/bar")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8001"))
                .build();
    }

}
