package com.idm.identity_recognition_process.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${identity.service.url}")
    private String identityServiceUrl;

    @Bean
    public WebClient identityWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(identityServiceUrl)
                .build();
    }
}

