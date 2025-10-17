package me.arcademadness.omni_dungeon.events;

import me.arcademadness.omni_dungeon.entities.Entity;
public class EntityDamageEvent extends BaseEvent {
    private final Entity target;
    private final Entity source;
    private int damage;
    private boolean canceled = false;

    public EntityDamageEvent(Entity target, Entity source, int damage) {
        this.target = target;
        this.source = source;
        this.damage = damage;
    }

    public Entity getTarget() { return target; }
    public Entity getSource() { return source; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    @Override
    public void cancel() { this.canceled = true; }

    @Override
    public boolean isCanceled() { return canceled; }

    @Override
    void execute() {
        target.getHealth().damage(damage);
    }
}

