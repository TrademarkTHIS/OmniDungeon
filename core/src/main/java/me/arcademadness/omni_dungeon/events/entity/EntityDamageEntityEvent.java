package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;
public class EntityDamageEntityEvent extends EntityDamageEvent {
    protected final Entity source;

    public EntityDamageEntityEvent(Entity target, Entity source, int damage) {
        super(target, damage);
        this.source = source;
    }

    public Entity getSource() { return source; }
}
