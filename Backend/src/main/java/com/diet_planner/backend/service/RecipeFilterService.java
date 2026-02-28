package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.*;
import com.diet_planner.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeFilterService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> filterRecipes(User user, MacroGoalRule rule) {

        List<Recipe> recipes = recipeRepository.findAll();

        // Appliance constraint
        if (Boolean.FALSE.equals(user.getHasCookingAppliance())) {
            recipes = recipes.stream()
                    .filter(r -> !Boolean.TRUE.equals(r.getRequiresAppliance()))
                    .toList();
        }

        // Weight loss constraint (simple MVP logic)
        if (user.getGoal() == Goal.WEIGHT_LOSS) {
            recipes = recipes.stream()
                    .filter(r -> r.getCalories() != null && r.getCalories() <= 500)
                    .toList();
        }

        return recipes;
    }
}