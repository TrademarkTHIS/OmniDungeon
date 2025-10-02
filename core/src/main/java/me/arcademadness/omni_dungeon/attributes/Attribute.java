package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;

public interface Attribute {
    double getBaseValue();
    double getFinalValue();
    void addModifier(AttributeModifier modifier);
    void removeModifier(AttributeModifier modifier);
}
