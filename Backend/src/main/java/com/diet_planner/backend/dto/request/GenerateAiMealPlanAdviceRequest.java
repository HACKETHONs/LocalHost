package com.diet_planner.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class GenerateAiMealPlanAdviceRequest {

    @NotNull
    private UUID userId;

    @Min(1)
    @Max(12)
    private int month;

    @Min(2024)
    private int year;

    @Min(3)
    @Max(30)
    private int days = 7;

    @Size(max = 400)
    private String preferences;
}
