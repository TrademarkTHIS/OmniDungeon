package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.controllers.goals.Goal;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class GoalController<T extends Entity> extends AbstractController<T> {

    private final List<Goal<T>> goals = new ArrayList<>();

    protected void addGoal(Goal<T> goal) {
        goals.add(goal);
        goals.sort(Comparator.comparingInt(Goal::getPriority));
    }

    @Override
    public Optional<ControlIntent> getIntent() {
        T entity = getEntity();
        for (Goal<T> goal : goals) {
            // Check if this goal should be active.
            if (goal.shouldActivate(entity)) {
                Optional<ControlIntent> intent = goal.computeIntent(entity);
                if (intent.isPresent()) return intent;
            }
        }
        return Optional.empty();
    }
}
