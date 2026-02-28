package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.Goal;
import com.diet_planner.backend.entity.MacroGoalRule;
import com.diet_planner.backend.exception.NotFoundException;
import com.diet_planner.backend.repository.MacroGoalRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MacroRuleService {

    private final MacroGoalRuleRepository repository;

    public MacroGoalRule getRuleForGoal(Goal goal) {
        return repository.findByGoal(goal)
                .orElseThrow(() -> new NotFoundException("Macro rule not found for goal"));
    }
}
