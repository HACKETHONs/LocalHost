package com.diet_planner.backend.controller;

import com.diet_planner.backend.dto.request.GenerateMealPlanRequest;
import com.diet_planner.backend.dto.request.GenerateAiMealPlanAdviceRequest;
import com.diet_planner.backend.dto.response.AiMealPlanAdviceResponse;
import com.diet_planner.backend.dto.response.MealPlanResponse;
import com.diet_planner.backend.entity.MealPlan;
import com.diet_planner.backend.mapper.MealPlanMapper;
import com.diet_planner.backend.service.AiMealPlanAdvisorService;
import com.diet_planner.backend.service.MealPlanGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanGenerationService service;
    private final AiMealPlanAdvisorService aiAdvisorService;

    @PostMapping("/generate")
    public MealPlanResponse generate(@Valid @RequestBody GenerateMealPlanRequest request) {

        MealPlan plan = service.generateMonthlyPlan(
                request.getUserId(),
                request.getMonth(),
                request.getYear()
        );

        return MealPlanMapper.toResponse(plan);
    }

    @PostMapping("/generate-ai-advice")
    public AiMealPlanAdviceResponse generateAiAdvice(@Valid @RequestBody GenerateAiMealPlanAdviceRequest request) {
        return aiAdvisorService.generateAdvice(request);
    }
}
