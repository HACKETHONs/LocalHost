package com.diet_planner.backend.service;

import com.diet_planner.backend.config.GroqProperties;
import com.diet_planner.backend.dto.request.GenerateAiMealPlanAdviceRequest;
import com.diet_planner.backend.dto.response.AiMealPlanAdviceResponse;
import com.diet_planner.backend.entity.MacroGoalRule;
import com.diet_planner.backend.entity.Recipe;
import com.diet_planner.backend.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiMealPlanAdvisorService {

    private static final String SYSTEM_PROMPT = """
            You are a nutrition and budget optimization planner for hostel students.
            Objective: maximize protein and macro quality, keep costs low, and keep meal variety practical.
            Always respond in valid JSON with keys: summary, recommendations, costSavingTips.
            recommendations and costSavingTips must be arrays of short strings.
            """;

    private final RestClient groqRestClient;
    private final GroqProperties groqProperties;
    private final UserService userService;
    private final MacroRuleService macroRuleService;
    private final RecipeFilterService recipeFilterService;
    private final ObjectMapper objectMapper;

    public AiMealPlanAdviceResponse generateAdvice(GenerateAiMealPlanAdviceRequest request) {
        User user = userService.getById(request.getUserId());
        MacroGoalRule rule = macroRuleService.getRuleForGoal(user.getGoal());
        List<Recipe> recipes = recipeFilterService.filterRecipes(user, rule);

        String userPrompt = buildUserPrompt(user, rule, recipes, request);

        String content = callGroq(userPrompt);
        JsonNode structured = parseModelContent(content);

        List<String> recommendations = toStringList(structured.get("recommendations"));
        if (recommendations.isEmpty()) {
            recommendations = List.of("Use the generated rule-based plan and rotate low-cost high-protein recipes.");
        }

        List<String> costSavingTips = toStringList(structured.get("costSavingTips"));
        if (costSavingTips.isEmpty()) {
            costSavingTips = List.of("Batch-cook once and reuse ingredients across meals to lower weekly cost.");
        }

        String summary = structured.path("summary").asText(
                "AI suggested a balanced low-cost, high-protein plan with better variety."
        );

        return AiMealPlanAdviceResponse.builder()
                .model(groqProperties.getModel())
                .summary(summary)
                .recommendations(recommendations)
                .costSavingTips(costSavingTips)
                .build();
    }

    private String callGroq(String userPrompt) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", groqProperties.getModel());
            payload.put("temperature", groqProperties.getTemperature());
            payload.put("messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),
                    Map.of("role", "user", "content", userPrompt)
            ));

            JsonNode response = groqRestClient.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                return "{}";
            }
            return response.path("choices").path(0).path("message").path("content").asText("{}");
        } catch (Exception ex) {
            return "{}";
        }
    }

    private JsonNode parseModelContent(String content) {
        try {
            String trimmed = content == null ? "" : content.trim();
            int start = trimmed.indexOf("{");
            int end = trimmed.lastIndexOf("}");
            if (start >= 0 && end > start) {
                return objectMapper.readTree(trimmed.substring(start, end + 1));
            }
            return objectMapper.createObjectNode();
        } catch (Exception ex) {
            return objectMapper.createObjectNode();
        }
    }

    private List<String> toStringList(JsonNode arrayNode) {
        List<String> list = new ArrayList<>();
        if (arrayNode == null || !arrayNode.isArray()) {
            return list;
        }
        for (JsonNode node : arrayNode) {
            if (node.isTextual()) {
                list.add(node.asText());
            }
        }
        return list;
    }

    private String buildUserPrompt(User user,
                                   MacroGoalRule rule,
                                   List<Recipe> recipes,
                                   GenerateAiMealPlanAdviceRequest request) {
        List<Recipe> topCandidates = recipes.stream()
                .filter(r -> r.getAvgCost() != null && r.getProtein() != null && r.getAvgCost() > 0)
                .sorted(Comparator.comparingDouble(this::proteinPerCost).reversed())
                .limit(15)
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Create hostel student meal guidance.\n");
        sb.append("User profile:\n");
        sb.append("- goal: ").append(user.getGoal()).append('\n');
        sb.append("- monthlyBudget: ").append(user.getMonthlyBudget()).append('\n');
        sb.append("- mealsPerDay: ").append(user.getMealsPerDay()).append('\n');
        sb.append("- hasCookingAppliance: ").append(user.getHasCookingAppliance()).append('\n');
        sb.append("- month/year: ").append(request.getMonth()).append("/").append(request.getYear()).append('\n');
        sb.append("- planningDays: ").append(request.getDays()).append('\n');
        sb.append("- extraPreferences: ")
                .append(request.getPreferences() == null || request.getPreferences().isBlank() ? "none" : request.getPreferences())
                .append('\n');
        sb.append("Macro rule:\n");
        sb.append("- proteinWeight: ").append(rule.getProteinWeight()).append('\n');
        sb.append("- carbsWeight: ").append(rule.getCarbsWeight()).append('\n');
        sb.append("- fatWeight: ").append(rule.getFatWeight()).append('\n');
        sb.append("- sugarWeight: ").append(rule.getSugarWeight()).append('\n');
        sb.append("- minProteinPerDay: ").append(rule.getMinProteinPerDay()).append('\n');
        sb.append("- maxCaloriesPerDay: ").append(rule.getMaxCaloriesPerDay()).append('\n');
        sb.append("Top recipe candidates (name|cost|protein|calories):\n");
        for (Recipe recipe : topCandidates) {
            sb.append("- ")
                    .append(recipe.getName()).append('|')
                    .append(recipe.getAvgCost()).append('|')
                    .append(recipe.getProtein()).append('|')
                    .append(recipe.getCalories()).append('\n');
        }
        sb.append("Return JSON only.");
        return sb.toString();
    }

    private double proteinPerCost(Recipe recipe) {
        return recipe.getProtein() / recipe.getAvgCost();
    }
}
