package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.attributes.Armor;
import me.arcademadness.omni_dungeon.attributes.Health;

public abstract class MobEntity extends BaseEntity {
    private Entity target;

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
