package com.diet_planner.backend.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ChatRequest {
    @NotBlank
    private String message;
}
