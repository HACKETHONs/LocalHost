package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PlanSchedulerService {

    private final RecipePickerService pickerService;

    public void populateMealPlan(MealPlan plan,
                                 List<Recipe> recipes,
                                 int mealsPerDay) {

        Map<MealType, Map<Integer, Set<UUID>>> historyByMealType = new EnumMap<>(MealType.class);
        Map<MealType, List<Recipe>> poolByMealType = buildPools(recipes);
        poolByMealType = personalizePools(plan, poolByMealType);

        for (int day = 1; day <= 30; day++) {

            PlanDay planDay = new PlanDay();
            planDay.setDayNumber(day);
            planDay.setMealPlan(plan);

            for (MealType mealType : MealType.values()) {

                Meal meal = new Meal();
                meal.setMealType(mealType);
                meal.setPlanDay(planDay);

                Map<Integer, Set<UUID>> mealHistory = historyByMealType.computeIfAbsent(mealType, k -> new HashMap<>());
                List<Recipe> mealPool = poolByMealType.getOrDefault(mealType, recipes);

                List<Recipe> options =
                        pickerService.pickRecipes(
                                mealPool,
                                mealHistory,
                                day,
                                6
                        );

                meal.setRecipeOptions(options);
                planDay.getMeals().add(meal);

                mealHistory.computeIfAbsent(day, d -> new HashSet<>())
                        .addAll(
                                options.stream().map(Recipe::getId).toList()
                        );
            }

            plan.getDays().add(planDay);
        }
    }

    private Map<MealType, List<Recipe>> buildPools(List<Recipe> recipes) {
        List<Recipe> breakfastPool = recipes.stream()
                .filter(this::isBreakfastRecipe)
                .toList();

        List<Recipe> nonBreakfastPool = recipes.stream()
                .filter(r -> !isBreakfastRecipe(r))
                .toList();

        Map<MealType, List<Recipe>> pools = new EnumMap<>(MealType.class);
        pools.put(MealType.BREAKFAST, breakfastPool.isEmpty() ? recipes : breakfastPool);
        pools.put(MealType.LUNCH, nonBreakfastPool.isEmpty() ? recipes : nonBreakfastPool);
        pools.put(MealType.DINNER, nonBreakfastPool.isEmpty() ? recipes : nonBreakfastPool);
        return pools;
    }

    private boolean isBreakfastRecipe(Recipe recipe) {
        String name = recipe.getName() == null ? "" : recipe.getName().toLowerCase(Locale.ROOT);
        return name.contains("poha")
                || name.contains("upma")
                || name.contains("idli")
                || name.contains("dosa")
                || name.contains("omelette")
                || name.contains("paratha")
                || name.contains("oats")
                || name.contains("milk")
                || name.contains("banana")
                || name.contains("egg bhurji")
                || name.contains("boiled eggs");
    }

    private Map<MealType, List<Recipe>> personalizePools(MealPlan plan, Map<MealType, List<Recipe>> poolByMealType) {
        long seedBase = 17L;
        if (plan.getUser() != null && plan.getUser().getId() != null) {
            seedBase = seedBase * 31 + plan.getUser().getId().hashCode();
        }
        seedBase = seedBase * 31 + (plan.getMonth() == null ? 0 : plan.getMonth());
        seedBase = seedBase * 31 + (plan.getYear() == null ? 0 : plan.getYear());

        Map<MealType, List<Recipe>> personalized = new EnumMap<>(MealType.class);
        for (Map.Entry<MealType, List<Recipe>> entry : poolByMealType.entrySet()) {
            List<Recipe> shuffled = new ArrayList<>(entry.getValue());
            long mealSeed = seedBase * 31 + entry.getKey().ordinal();
            Collections.shuffle(shuffled, new Random(mealSeed));
            personalized.put(entry.getKey(), shuffled);
        }
        return personalized;
    }
}
