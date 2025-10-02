package me.arcademadness.omni_dungeon.modifiers;

import me.arcademadness.omni_dungeon.attributes.AttributeType;

public interface AttributeModifier {
    double modify(double currentValue);
    ModifierType getType();
    AttributeType getAttributeType();

    enum ModifierType {
        FLAT, PERCENT, MULTIPLY
    }
}
