package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.events.BaseEvent;

public abstract class EntityEvent extends BaseEvent {

    protected Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
