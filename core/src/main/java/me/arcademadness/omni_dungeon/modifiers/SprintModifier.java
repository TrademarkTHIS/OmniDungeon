package me.arcademadness.omni_dungeon.modifiers;

import me.arcademadness.omni_dungeon.attributes.AttributeType;

public class SprintModifier implements AttributeModifier<Float> {

    private int mul;
    private boolean isEnabled = false;

    public SprintModifier(int amount) {
        mul = amount;
    }

    public SprintModifier() {
        mul = 2;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public Float modify(Float currentValue) {
        if (isEnabled) {
            return currentValue * mul;
        }
        return currentValue;
    }

    @Override
    public AttributeType getAttributeType() { return AttributeType.MAX_SPEED; }
}

