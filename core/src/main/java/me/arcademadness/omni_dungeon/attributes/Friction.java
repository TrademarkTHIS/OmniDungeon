package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;

public final class Friction extends SimpleAttribute<Float> {
    public Friction(float value) {
        super(value);
    }

    public Friction(MaxSpeed speed, float ratio) {
        super(0f);
        addModifier(new AttributeModifier<>() {
            @Override
            public Float modify(Float currentValue) {
                return currentValue + speed.getBaseValue() * ratio;
            }

            @Override
            public AttributeType getAttributeType() {
                return AttributeType.FRICTION;
            }
        });
    }

}
