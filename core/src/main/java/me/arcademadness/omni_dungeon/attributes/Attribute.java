package me.arcademadness.omni_dungeon.attributes;

import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import java.util.List;

/**
 * Represents a numeric attribute belonging to an entity or system component.
 * <p>
 * Attributes define a base numeric value and can be modified by one or more
 * {@link AttributeModifier AttributeModifiers} that alter the effective (final) value.
 * Typical examples include attributes such as health, strength, speed, or defense.
 * </p>
 *
 * <p>
 * Implementations of this interface are responsible for managing their modifier list,
 * applying modifiers in order, and producing a computed {@link #getFinalValue()}.
 * </p>
 *
 * @param <T> the numeric type of this attribute (e.g. {@link Integer}, {@link Float}, {@link Double})
 */
public interface Attribute<T extends Number> {

    /**
     * Returns the unmodified, base value of this attribute.
     *
     * @return the base numeric value
     */
    T getBaseValue();

    /**
     * Returns the final, modified value of this attribute after applying all
     * active {@link AttributeModifier AttributeModifiers}.
     *
     * @return the computed final numeric value
     */
    T getFinalValue();

    /**
     * Adds a modifier to this attribute.
     * <p>
     * Modifiers are typically applied in insertion order unless otherwise
     * handled by the implementation.
     * </p>
     *
     * @param modifier the modifier to add
     */
    void addModifier(AttributeModifier<T> modifier);

    /**
     * Removes a modifier from this attribute.
     *
     * @param modifier the modifier to remove
     */
    void removeModifier(AttributeModifier<T> modifier);

    /**
     * Moves an existing modifier to a new position in the modifier list.
     * <p>
     * This can be used to control the order of modifier application.
     * </p>
     *
     * @param modifier the modifier to move
     * @param index    the new index position for the modifier
     */
    void moveModifier(AttributeModifier<T> modifier, int index);

    /**
     * Returns all modifiers of the specified type that are currently applied
     * to this attribute.
     *
     * @param modifierClass the modifier type to filter by
     * @return a list of modifiers matching the given class
     */
    List<AttributeModifier<T>> getModifiersByType(Class<? extends AttributeModifier<T>> modifierClass);
}
