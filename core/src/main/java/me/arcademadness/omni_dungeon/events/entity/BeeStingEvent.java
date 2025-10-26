package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class BeeStingEvent extends EntityDamageEntityEvent {

    public BeeStingEvent(Entity target, BeeEntity bee, int damage) {
        super(target, bee, damage);
    }

    @Override
    protected void execute() {
        super.execute();
        EntityDamageEvent damageSelf = new EntityDamageEntityEvent(source, source,1);

        EnvironmentView env = entity.getEnvironment();
        if (env != null) {
            env.getEventBus().post(damageSelf);
        }
    }
}

