package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import java.util.ArrayList;
import java.util.List;

public abstract class SimpleAttribute<T extends Number> implements Attribute<T> {

    protected final List<AttributeModifier<T>> modifiers = new ArrayList<>();
    protected T baseValue;

    public SimpleAttribute(T baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public T getBaseValue() {
        return baseValue;
    }

    @Override
    public T getFinalValue() {
        T finalVal = baseValue;
        for (AttributeModifier<T> modifier : modifiers) {
            finalVal = modifier.modify(finalVal);
        }
        return finalVal;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        modifiers.add(0, modifier);
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        modifiers.remove(modifier);
    }

    @Override
    public void moveModifier(AttributeModifier modifier, int index) {
        if (modifiers.remove(modifier)) {
            modifiers.add(Math.max(0, Math.min(index, modifiers.size())), modifier);
        }
    }

    @Override
    public List<AttributeModifier> getModifiersByType(Class<? extends AttributeModifier> modifierClass) {
        List<AttributeModifier> targetModifiers = new ArrayList<>();
        for (AttributeModifier modifier : modifiers) {
            if (modifierClass.isInstance(modifier)) {
                targetModifiers.add(modifier);
            }
        }
        return targetModifiers;
    }

    public <M extends AttributeModifier> M getFirstModifier(Class<M> modifierClass) {
        for (AttributeModifier modifier : modifiers) {
            if (modifierClass.isInstance(modifier)) {
                return modifierClass.cast(modifier);
            }
        }
        return null;
    }
}
