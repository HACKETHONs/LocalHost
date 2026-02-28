package com.diet_planner.backend.dto.response;

import com.diet_planner.backend.entity.MealType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MealResponse {

    private MealType mealType;
    private List<RecipeOptionResponse> options;
}