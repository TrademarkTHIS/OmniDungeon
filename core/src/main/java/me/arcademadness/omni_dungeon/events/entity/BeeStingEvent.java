package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.attributes.AttributeType;
import me.arcademadness.omni_dungeon.attributes.modifiers.AttributeModifier;
import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class BeeStingEvent extends EntityDamageEntityEvent {

    private static final int ARMOR_TICKS = 40;

    public BeeStingEvent(Entity target, BeeEntity bee, int damage) {
        super(target, bee, damage);
    }

    @Override
    protected void execute() {
        EnvironmentView env = entity.getEnvironment();
        if (env != null) {
            BeeStingArmorModifier modifier = entity.getArmor().getOrCreateModifier(
                BeeStingArmorModifier.class,
                () -> new BeeStingArmorModifier(env)
            );
            modifier.refresh();
            modifier.addHit();
        }

        super.execute();

        EntityDamageEvent damageSelf = new EntityDamageEntityEvent(source, source, 1);
        if (env != null) {
            env.getEventBus().post(damageSelf);
        }
    }

    public static class BeeStingArmorModifier implements AttributeModifier<Integer> {
        private final EnvironmentView env;
        private long lastAppliedTick;
        private int totalHits;

        public BeeStingArmorModifier(EnvironmentView env) {
            this.env = env;
            this.lastAppliedTick = env.getCurrentTick();
            this.totalHits = 0;
        }

        public void refresh() {
            lastAppliedTick = env.getCurrentTick();
        }

        public void addHit() {
            totalHits++;
        }

        public int getTotalHits() {
            return totalHits;
        }

        @Override
        public Integer modify(Integer currentValue) {
            return Math.max(currentValue - totalHits, 0);
        }

        @Override
        public AttributeType getAttributeType() {
            return AttributeType.ARMOR;
        }

        @Override
        public boolean isExpired() {
            return env.getCurrentTick() - lastAppliedTick >= ARMOR_TICKS;
        }
    }
}
