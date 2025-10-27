package me.arcademadness.omni_dungeon.modifiers;

import me.arcademadness.omni_dungeon.attributes.AttributeType;

/**
 * Represents a modifier that can change the value of a specific attribute.
 * <p>
 * {@link AttributeModifier} allows dynamic adjustments to an entity's attributes,
 * such as health, mana, or speed, by applying a modification to the current value.
 * </p>
 *
 * @param <T> the numeric type of the attribute being modified (e.g., Integer, Float)
 * @see me.arcademadness.omni_dungeon.attributes.Attribute
 * @see me.arcademadness.omni_dungeon.entities.Entity
 */
public interface AttributeModifier<T extends Number> {

    /**
     * Modifies the current value of the attribute.
     * <p>
     * Implementations define the logic for how the value is altered, such as adding,
     * multiplying, or applying more complex formulas.
     * </p>
     *
     * @param currentValue the current value of the attribute before modification
     * @return the new modified value
     */
    T modify(T currentValue);

    /**
     * Returns the type of attribute this modifier affects.
     *
     * @return the {@link AttributeType} of the attribute being modified
     */
    AttributeType getAttributeType();
}
