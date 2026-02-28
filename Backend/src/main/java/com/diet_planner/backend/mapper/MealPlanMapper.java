package com.diet_planner.backend.mapper;

import com.diet_planner.backend.dto.response.*;
import com.diet_planner.backend.entity.*;

import java.util.stream.Collectors;

public class MealPlanMapper {

    public static MealPlanResponse toResponse(MealPlan plan) {

        return MealPlanResponse.builder()
                .planId(plan.getId())
                .month(plan.getMonth())
                .year(plan.getYear())
                .budget(plan.getBudget())
                .mealsPerDay(plan.getMealsPerDay())
                .days(
                        plan.getDays().stream().map(day ->
                                PlanDayResponse.builder()
                                        .dayNumber(day.getDayNumber())
                                        .meals(
                                                day.getMeals().stream().map(meal ->
                                                        MealResponse.builder()
                                                                .mealType(meal.getMealType())
                                                                .options(
                                                                        meal.getRecipeOptions().stream().map(recipe ->
                                                                                RecipeOptionResponse.builder()
                                                                                        .id(recipe.getId())
                                                                                        .name(recipe.getName())
                                                                                        .avgCost(recipe.getAvgCost())
                                                                                        .calories(recipe.getCalories())
                                                                                        .protein(recipe.getProtein())
                                                                                        .build()
                                                                        ).collect(Collectors.toList())
                                                                )
                                                                .build()
                                                ).collect(Collectors.toList())
                                        )
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}