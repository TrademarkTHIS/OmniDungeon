package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import java.util.List;

public interface Attribute<T extends Number> {
    T getBaseValue();
    T getFinalValue();
    void addModifier(AttributeModifier<T> modifier);
    void removeModifier(AttributeModifier<T> modifier);
    void moveModifier(AttributeModifier<T> modifier, int index);
    List<AttributeModifier<T>> getModifiersByType(Class<? extends AttributeModifier<T>> modifierClass);
}
