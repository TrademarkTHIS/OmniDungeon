package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.services.CollisionService;
import me.arcademadness.omni_dungeon.environment.services.MovementService;
import me.arcademadness.omni_dungeon.events.EventBus;

import java.util.List;

/**
 * Provides a read-only view of the game environment.
 * <p>
 * {@link EnvironmentView} exposes the current state of the environment,
 * including the map, entities, and services such as collision and movement handling.
 * It also provides methods to spawn or despawn entities at specific locations.
 * </p>
 * <p>
 * For full control of the environment (updating state or adding/removing entities),
 * see {@link EnvironmentControl}.
 * </p>
 */
public interface EnvironmentView {

    /**
     * Returns the tile map representing the environment's layout.
     *
     * @return the {@link TileMap} of the environment
     * @see EnvironmentControl
     */
    TileMap getMap();

    /**
     * Returns a list of entities currently present in the environment.
     *
     * @return a {@link List} of {@link Entity} objects
     * @see EnvironmentControl#addEntity(Entity)
     * @see EnvironmentControl#removeEntity(Entity)
     */
    List<Entity> getEntities();

    /**
     * Returns the collision service responsible for handling entity collisions.
     *
     * @return the {@link CollisionService} for the environment
     */
    CollisionService getCollisionService();

    /**
     * Returns the movement service responsible for moving entities in the environment.
     *
     * @return the {@link MovementService} for the environment
     */
    MovementService getMovementService();

    /**
     * Returns the event bus used for broadcasting and listening to game events.
     *
     * @return the {@link EventBus} of the environment
     */
    EventBus getEventBus();

    /**
     * Spawns an entity at the specified location in the environment.
     * <p>
     * After spawning, the entity will appear in {@link #getEntities()}.
     * </p>
     *
     * @param entity   the {@link Entity} to spawn
     * @param location the {@link Location} at which to spawn the entity
     * @see EnvironmentControl#addEntity(Entity)
     */
    void spawn(Entity entity, Location location);

    /**
     * Despawns an entity from the environment.
     * <p>
     * After despawning, the entity will no longer appear in {@link #getEntities()}.
     * </p>
     *
     * @param entity the {@link Entity} to remove
     * @see EnvironmentControl#removeEntity(Entity)
     */
    void despawn(Entity entity);
}
