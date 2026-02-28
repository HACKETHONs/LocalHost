package com.diet_planner.backend.repository;

import com.diet_planner.backend.entity.Goal;
import com.diet_planner.backend.entity.MacroGoalRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MacroGoalRuleRepository extends JpaRepository<MacroGoalRule, UUID> {

    Optional<MacroGoalRule> findByGoal(Goal goal);
}