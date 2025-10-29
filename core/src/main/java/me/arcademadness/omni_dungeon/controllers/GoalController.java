package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.controllers.goals.Goal;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Provides ControlIntents based on {@link Goal}s rather than hard coded logic.
 * @param <T> the type of mob that will use this controller
 */
public abstract class GoalController<T extends Entity> extends AbstractController<T> {

    private final List<Goal<T>> goals = new ArrayList<>();

    public void addGoal(Goal<T> goal) {
        goals.add(goal);
        goals.sort(Comparator.comparingInt(Goal::getPriority));
    }

    @Override
    public Optional<ControlIntent> getIntent() {
        T entity = getEntity();
        List<Goal<T>> copy = new ArrayList<Goal<T>>(goals);
        for (Goal<T> goal : copy) {
            // Check if this goal should be active.
            if (goal.shouldActivate(entity)) {
                Optional<ControlIntent> intent = goal.computeIntent(entity);
                if (intent.isPresent()) return intent;
            }
        }
        return Optional.empty();
    }

    public <G extends Goal<?>> boolean hasGoal(Class<G> goalClass) {
        for (Goal<T> goal : goals) {
            if (goalClass.isInstance(goal)) {
                return true;
            }
        }
        return false;
    }

    public void removeGoal(Goal<T> goal) {
        goals.remove(goal);
        goals.sort(Comparator.comparingInt(Goal::getPriority));
    }

    public <G extends Goal<?>> void removeGoal(Class<G> goalClass) {
        goals.removeIf(goalClass::isInstance);
    }

}
