package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleAttribute implements Attribute {

    protected final List<AttributeModifier> modifiers = new ArrayList<>();
    protected double baseValue;

    public SimpleAttribute(double baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public double getBaseValue() {
        return baseValue;
    }

    @Override
    public double getFinalValue() {
        double finalVal = baseValue;
        for (AttributeModifier modifier : modifiers) {
            finalVal = modifier.modify(finalVal);
        }
        return finalVal;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        modifiers.addFirst(modifier);
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        modifiers.remove(modifier);
    }
}
