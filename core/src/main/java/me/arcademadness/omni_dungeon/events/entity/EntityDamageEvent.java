package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class EntityDamageEvent extends EntityEvent {
    private int damage;

    public EntityDamageEvent(Entity target, int damage) {
        super(target);
        setDamage(damage);
    }

    public int getDamage() { return damage; }

    public void setDamage(int damage) {
        this.damage = Math.max(damage,0);
    }

    @Override
    protected void execute() {
        entity.getHealth().damage(damage);

        double newHealth = entity.getHealth().getCurrentHealth();
        if (newHealth <= 0) {
            EnvironmentView env = entity.getEnvironment();
            if (env != null) {
                env.getEventBus().post(new EntityDeathEvent(entity));
            }
        }
    }
}

