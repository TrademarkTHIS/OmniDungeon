package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import java.util.List;

public interface Attribute<T extends Number> {
    T getBaseValue();
    T getFinalValue();
    void addModifier(AttributeModifier modifier);
    void removeModifier(AttributeModifier modifier);
    void moveModifier(AttributeModifier modifier, int index);
    List<AttributeModifier> getModifiersByType(Class<? extends AttributeModifier> modifierClass);
}
