package me.arcademadness.omni_dungeon.modifiers;

import me.arcademadness.omni_dungeon.attributes.AttributeType;

public class SprintModifier implements AttributeModifier {
    @Override
    public double modify(double currentValue) {
        return currentValue * 2;
    }

    @Override
    public ModifierType getType() { return ModifierType.MULTIPLY; }

    @Override
    public AttributeType getAttributeType() { return AttributeType.MAX_SPEED; }
}

