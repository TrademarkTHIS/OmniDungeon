package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;

public class EntityDespawnEvent extends EntityEvent {

    private final Environment env;

    public EntityDespawnEvent(Entity entity, Environment env) {
        super(entity);
        this.env = env;
    }

    @Override
    protected void execute() {
        if (env != null) {
            env.removeEntity(entity);
        }
    }
}
