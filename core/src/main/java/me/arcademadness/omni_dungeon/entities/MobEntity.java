package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.components.MobGroup;

public abstract class MobEntity extends BaseEntity {
    private Entity target;
    private MobGroup group = null;

    public MobGroup getGroup() { return group; }
    public void setGroup(MobGroup group) { this.group = group; }


    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
