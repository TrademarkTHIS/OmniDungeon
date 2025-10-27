package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.entities.Entity;

/**
 * Extends {@link EnvironmentView} to provide full control over the environment.
 * <p>
 * {@link EnvironmentControl} allows updating the environment state, as well as adding or removing
 * entities from the environment. It builds on {@link EnvironmentView}, which provides
 * read-only access to environment data and services.
 * </p>
 */
public interface EnvironmentControl extends EnvironmentView {

    /**
     * Updates the environment state based on the elapsed time.
     * <p>
     * This method is typically called once per game tick or frame to advance
     * all simulations, such as entity movement, collisions, or environmental effects.
     * </p>
     *
     * @param delta the time elapsed since the last update, in seconds
     * @see EnvironmentView#getEntities()
     * @see EnvironmentView#getCollisionService()
     * @see EnvironmentView#getMovementService()
     */
    void tick(float delta);

    /**
     * Adds an entity to the environment.
     * <p>
     * After adding, the entity will be included in {@link EnvironmentView#getEntities()}.
     * </p>
     *
     * @param e the {@link Entity} to add
     * @see EnvironmentView#spawn(Entity, me.arcademadness.omni_dungeon.components.Location)
     */
    void addEntity(Entity e);

    /**
     * Removes an entity from the environment.
     * <p>
     * After removal, the entity will no longer appear in {@link EnvironmentView#getEntities()}.
     * </p>
     *
     * @param e the {@link Entity} to remove
     * @see EnvironmentView#despawn(Entity)
     */
    void removeEntity(Entity e);
}
