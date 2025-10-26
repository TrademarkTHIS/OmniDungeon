package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class EntityDeathEvent extends EntityEvent {
    public EntityDeathEvent(Entity entity) {
        super(entity);
    }

    @Override
    protected void execute() {
        EnvironmentView env = entity.getEnvironment();
        if (env != null) {
            env.despawn(entity);
        }
    }
}
