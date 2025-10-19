package me.arcademadness.omni_dungeon.actions;

import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.entities.Entity;

/**
 * Represents an executable action that an entity can perform within an {@link Environment}.
 * <p>
 * Actions define behavior such as moving, attacking, or interacting with the world.
 * Each action determines whether it can be executed and performs logic when executed.
 * </p>
 */
public interface Action {
    /**
     * Checks whether this action can be executed by the specified entity in the given environment.
     *
     * @param environment the environment in which the action would occur
     * @param entity the entity attempting to perform the action
     * @return {@code true} if the action can be executed, otherwise {@code false}
     */
    boolean canExecute(Environment environment, Entity entity);

    /**
     * Executes this action for the specified entity.
     *
     * @param environment the environment in which the action occurs
     * @param entity the entity performing the action
     * @param delta the frame delta time, used for time-based updates
     */
    void execute(Environment environment, Entity entity, float delta);
}
