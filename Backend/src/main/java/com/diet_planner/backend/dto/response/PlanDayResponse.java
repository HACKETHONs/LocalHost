package com.diet_planner.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlanDayResponse {

    private Integer dayNumber;
    private List<MealResponse> meals;
}