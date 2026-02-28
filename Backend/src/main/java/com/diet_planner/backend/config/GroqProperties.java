package com.diet_planner.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "groq.ai")
public class GroqProperties {

    private String apiKey = "";
    private String baseUrl = "https://api.groq.com/openai";
    private String model = "llama-3.3-70b-versatile";
    private Double temperature = 0.2;
}
