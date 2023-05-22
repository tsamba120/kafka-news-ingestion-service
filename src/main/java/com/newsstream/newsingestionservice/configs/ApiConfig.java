package com.newsstream.newsingestionservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="services")
public class ApiConfig {
    @Value("${services.news-api-client.apiUrl}")
    String apiUrl;

    @Value("${services.news-api-client.apiKey}")
    String apiKey;

    public String getApiUrl() {
        return this.apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}
