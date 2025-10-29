package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.components.MobGroup;

import java.util.Optional;

public class CreateGroupGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int MAX_GROUP_SIZE;

    public CreateGroupGoal(int priority, int maxGroupSize) {
        this.priority = priority;
        MAX_GROUP_SIZE = maxGroupSize;
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
        MobGroup group = new MobGroup(MAX_GROUP_SIZE);
        if (entity.getEnvironment()!=null) {
            entity.getEnvironment().getEventBus().register(group);
        }
        group.addMember(entity);
        entity.setGroup(group);
        return Optional.empty();
    }
}
