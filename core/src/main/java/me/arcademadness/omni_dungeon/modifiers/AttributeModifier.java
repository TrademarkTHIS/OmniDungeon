package me.arcademadness.omni_dungeon.modifiers;

import me.arcademadness.omni_dungeon.attributes.AttributeType;

public interface AttributeModifier<T extends Number> {
    T modify(T currentValue);
    AttributeType getAttributeType();
}
