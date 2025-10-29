package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.components.MobGroup;

import java.util.Optional;

public class CreateGroupGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;

    public CreateGroupGoal(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        // Activate if no group exists (and FindGroup didn't find one)
        return entity.getGroup() == null;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        MobGroup group = new MobGroup();
        group.addMember(entity);
        entity.setGroup(group);
        return Optional.empty(); // group creation is purely internal
    }
}
