package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.Recipe;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipePickerService {

    public List<Recipe> pickRecipes(
            List<Recipe> allRecipes,
            Map<Integer, Set<UUID>> history,
            int currentDay,
            int count
    ) {
        return allRecipes.stream()
                .filter(r -> !usedInLastTwoDays(r.getId(), history, currentDay))
                .limit(count)
                .toList();
    }

    private boolean usedInLastTwoDays(UUID recipeId,
                                      Map<Integer, Set<UUID>> history,
                                      int day) {
        for (int i = day - 1; i >= day - 2; i--) {
            if (history.getOrDefault(i, Set.of()).contains(recipeId)) {
                return true;
            }
        }
        return false;
    }
}