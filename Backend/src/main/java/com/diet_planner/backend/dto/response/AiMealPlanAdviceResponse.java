package com.diet_planner.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiMealPlanAdviceResponse {

    private String model;
    private String summary;
    private List<String> recommendations;
    private List<String> costSavingTips;
}
