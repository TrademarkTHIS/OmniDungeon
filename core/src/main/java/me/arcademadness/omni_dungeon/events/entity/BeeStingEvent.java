package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.attributes.AttributeType;
import me.arcademadness.omni_dungeon.attributes.modifiers.AttributeModifier;
import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class BeeStingEvent extends EntityDamageEntityEvent {

    public BeeStingEvent(Entity target, BeeEntity bee, int damage) {
        super(target, bee, damage);
    }

    @Override
    protected void execute() {
        entity.getArmor().addModifier(new AttributeModifier<>() {
            final EnvironmentView env = entity.getEnvironment();
            final long creationTick = env.getCurrentTick();

            @Override
            public Integer modify(Integer currentValue) {
                return Math.max(currentValue - 1, 0);
            }

            @Override
            public AttributeType getAttributeType() {
                return AttributeType.ARMOR;
            }

            @Override
            public boolean isExpired() {
                return env.getCurrentTick() - creationTick >= 10;
            }
        });

        super.execute();
        EntityDamageEvent damageSelf = new EntityDamageEntityEvent(source, source,1);

        EnvironmentView env = entity.getEnvironment();
        if (env != null) {
            env.getEventBus().post(damageSelf);
        }
    }
}

