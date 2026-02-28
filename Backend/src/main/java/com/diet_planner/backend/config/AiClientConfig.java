package com.diet_planner.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GroqProperties.class)
public class AiClientConfig {

    @Bean
    public RestClient groqRestClient(RestClient.Builder builder, GroqProperties properties) {
        String baseUrl = normalizeBaseUrl(properties.getBaseUrl());
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private String normalizeBaseUrl(String configuredBaseUrl) {
        String fallback = "https://api.groq.com/openai";
        if (!StringUtils.hasText(configuredBaseUrl)) {
            return fallback;
        }
        String url = configuredBaseUrl.trim();
        if (url.endsWith("/v1/chat/completions")) {
            return url.substring(0, url.length() - "/v1/chat/completions".length());
        }
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
