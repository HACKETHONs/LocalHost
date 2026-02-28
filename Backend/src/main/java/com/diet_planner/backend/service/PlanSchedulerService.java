package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanSchedulerService {

    private final RecipePickerService pickerService;

    public void populateMealPlan(MealPlan plan,
                                 List<Recipe> recipes,
                                 int mealsPerDay) {

        Map<Integer, Set<UUID>> history = new HashMap<>();

        for (int day = 1; day <= 30; day++) {

            PlanDay planDay = new PlanDay();
            planDay.setDayNumber(day);
            planDay.setMealPlan(plan);

            for (MealType mealType : MealType.values()) {

                Meal meal = new Meal();
                meal.setMealType(mealType);
                meal.setPlanDay(planDay);

                List<Recipe> options =
                        pickerService.pickRecipes(
                                recipes,
                                history,
                                day,
                                6
                        );

                meal.setRecipeOptions(options);
                planDay.getMeals().add(meal);

                history.computeIfAbsent(day, d -> new HashSet<>())
                        .addAll(
                                options.stream().map(Recipe::getId).toList()
                        );
            }

            plan.getDays().add(planDay);
        }
    }
}