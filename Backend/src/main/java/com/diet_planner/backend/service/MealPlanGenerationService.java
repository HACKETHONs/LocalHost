package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.*;
import com.diet_planner.backend.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MealPlanGenerationService {

    private final UserService userService;
    private final MacroRuleService macroRuleService;
    private final RecipeFilterService recipeFilterService;
    private final PlanSchedulerService schedulerService;
    private final MealPlanRepository mealPlanRepository;

    public MealPlan generateMonthlyPlan(UUID userId, int month, int year) {
        var existing = mealPlanRepository.findTopByUserIdAndMonthAndYearOrderByVersionDesc(userId, month, year);
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = userService.getById(userId);
        MacroGoalRule rule = macroRuleService.getRuleForGoal(user.getGoal());

        var allowedRecipes =
                recipeFilterService.filterRecipes(user, rule);

        MealPlan plan = new MealPlan();
        plan.setUser(user);
        plan.setMonth(month);
        plan.setYear(year);
        plan.setBudget(user.getMonthlyBudget());
        plan.setMealsPerDay(user.getMealsPerDay());
        plan.setVersion(1);
        plan.setStatus("DRAFT");

        schedulerService.populateMealPlan(
                plan,
                allowedRecipes,
                user.getMealsPerDay()
        );

        return mealPlanRepository.save(plan);
    }
}
