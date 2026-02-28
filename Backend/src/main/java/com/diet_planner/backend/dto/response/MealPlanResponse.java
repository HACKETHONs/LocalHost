package com.diet_planner.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MealPlanResponse {

    private UUID planId;
    private Integer month;
    private Integer year;
    private Integer budget;
    private Integer mealsPerDay;

    private List<PlanDayResponse> days;
}