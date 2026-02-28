package com.diet_planner.backend.controller;

import com.diet_planner.backend.dto.request.ChatRequest;
import com.diet_planner.backend.dto.response.ChatResponse;
import com.diet_planner.backend.dto.response.RecipeOptionResponse;
import com.diet_planner.backend.entity.Recipe;
import com.diet_planner.backend.repository.RecipeRepository;
import com.diet_planner.backend.service.AiMealPlanAdvisorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final AiMealPlanAdvisorService aiAdvisorService;
    private final RecipeRepository recipeRepository;

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String message = request.getMessage().trim();
        String normalized = message.toLowerCase(Locale.ROOT);
        boolean needsRecipeOptions = normalized.contains("recipe")
                || normalized.contains("option")
                || normalized.contains("breakfast")
                || normalized.contains("lunch")
                || normalized.contains("dinner");

        List<RecipeOptionResponse> options = needsRecipeOptions
                ? getRecipeOptionsForIntent(message, normalized, 6)
                : List.of();

        String answer;
        if (needsRecipeOptions) {
            answer = aiAdvisorService.chat(buildRecipeAwarePrompt(message, normalized, options));
            if (answer == null || answer.isBlank()) {
                answer = options.isEmpty()
                        ? "I couldn't find recipe options right now."
                        : "I found recipe options. Tap any card for details, or ask for more by meal type.";
            }
        } else {
            answer = aiAdvisorService.chat(message);
            if (answer == null || answer.isBlank()) {
                answer = "I can help with meal planning, recipe ideas and cost-friendly options. Ask me for recipe options to get started.";
            }
        }

        return ChatResponse.builder()
                .answer(answer)
                .recipes(options)
                .quickActions(defaultQuickActions())
                .build();
    }

    @GetMapping("/chat/recipe-options")
    public List<RecipeOptionResponse> recipeOptions(@RequestParam(defaultValue = "6") int limit,
                                                    @RequestParam(required = false) String query) {
        return getRecipeOptionsInternal(limit, query);
    }

    private List<RecipeOptionResponse> getRecipeOptionsInternal(int limit, String query) {
        int safeLimit = Math.max(1, Math.min(limit, 12));
        String normalizedQuery = query == null ? null : query.trim().toLowerCase(Locale.ROOT);

        List<RecipeOptionResponse> options = new ArrayList<>();
        for (Recipe recipe : recipeRepository.findAll()) {
            if (normalizedQuery != null && !normalizedQuery.isBlank()) {
                String name = recipe.getName() == null ? "" : recipe.getName().toLowerCase(Locale.ROOT);
                if (!name.contains(normalizedQuery)) {
                    continue;
                }
            }
            options.add(RecipeOptionResponse.builder()
                    .id(recipe.getId())
                    .name(recipe.getName())
                    .avgCost(recipe.getAvgCost())
                    .protein(recipe.getProtein())
                    .calories(recipe.getCalories())
                    .build());
            if (options.size() >= safeLimit) {
                break;
            }
        }
        return options;
    }

    private List<RecipeOptionResponse> getRecipeOptionsForIntent(String message, String normalized, int limit) {
        List<RecipeOptionResponse> options = getRecipeOptionsInternal(limit, null);
        if (options.isEmpty()) {
            return options;
        }

        if (normalized.contains("budget") || normalized.contains("cheap") || normalized.contains("low cost")) {
            options = options.stream()
                    .sorted(Comparator.comparingInt(o -> o.getAvgCost() == null ? Integer.MAX_VALUE : o.getAvgCost()))
                    .toList();
        } else if (normalized.contains("protein")) {
            options = options.stream()
                    .sorted((a, b) -> Double.compare(
                            b.getProtein() == null ? 0.0 : b.getProtein(),
                            a.getProtein() == null ? 0.0 : a.getProtein()))
                    .toList();
        }

        String lowerMessage = message.toLowerCase(Locale.ROOT);
        if (lowerMessage.contains("breakfast")) {
            return options.stream()
                    .filter(this::looksBreakfast)
                    .limit(limit)
                    .toList();
        }
        if (lowerMessage.contains("lunch") || lowerMessage.contains("dinner")) {
            return options.stream()
                    .filter(o -> !looksBreakfast(o))
                    .limit(limit)
                    .toList();
        }

        return options.stream().limit(limit).toList();
    }

    private boolean looksBreakfast(RecipeOptionResponse option) {
        String name = option.getName() == null ? "" : option.getName().toLowerCase(Locale.ROOT);
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

    private String buildRecipeAwarePrompt(String userMessage, String normalizedUserMessage, List<RecipeOptionResponse> options) {
        boolean includeSteps = normalizedUserMessage.contains("step")
                || normalizedUserMessage.contains("how to")
                || normalizedUserMessage.contains("make")
                || normalizedUserMessage.contains("cook")
                || normalizedUserMessage.contains("breakfast")
                || normalizedUserMessage.contains("lunch")
                || normalizedUserMessage.contains("dinner");

        StringBuilder prompt = new StringBuilder();
        prompt.append("User message: ").append(userMessage).append('\n');
        prompt.append("Available recipe options with macros:\n");
        for (RecipeOptionResponse option : options) {
            prompt.append("- ")
                    .append(option.getName())
                    .append(" | cost: ")
                    .append(option.getAvgCost() == null ? "-" : option.getAvgCost())
                    .append(" | protein: ")
                    .append(option.getProtein() == null ? "-" : option.getProtein())
                    .append(" | calories: ")
                    .append(option.getCalories() == null ? "-" : option.getCalories())
                    .append('\n');
        }
        prompt.append("Use only these options for recommendations.\n");
        prompt.append("Answer briefly and recommend 2-4 best options based on the user ask.\n");
        if (includeSteps) {
            prompt.append("Also include a section 'Steps' with 5-7 numbered cooking steps for the best-matching recipe.\n");
            prompt.append("If the user asks for breakfast, prioritize breakfast-style options.");
        }
        return prompt.toString();
    }

    private List<String> defaultQuickActions() {
        return List.of(
                "Show recipe options",
                "Budget friendly recipes",
                "High protein recipes",
                "Improve my plan"
        );
    }
}
