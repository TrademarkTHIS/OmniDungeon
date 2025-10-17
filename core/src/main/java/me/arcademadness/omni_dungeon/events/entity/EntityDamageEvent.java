package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;

public class EntityDamageEvent extends EntityEvent {
    private int damage;

    public EntityDamageEvent(Entity target, int damage) {
        super(target);
        this.damage = damage;
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    @Override
    protected void execute() {
        entity.getHealth().damage(damage);
    }
}

