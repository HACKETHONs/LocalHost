package com.diet_planner.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class GenerateMealPlanRequest {

    @NotNull
    private UUID userId;

    @Min(1)
    @Max(12)
    private int month;

    @Min(2024)
    private int year;
}
