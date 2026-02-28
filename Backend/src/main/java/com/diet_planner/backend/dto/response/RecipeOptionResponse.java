package com.diet_planner.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecipeOptionResponse {

    private UUID id;
    private String name;
    private Integer avgCost;
    private Double calories;
    private Double protein;
}