package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.*;
import me.arcademadness.omni_dungeon.controllers.Controller;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

import java.util.Set;

/**
 * Represents a dynamic entity within the game world.
 * <p>
 * Entities encapsulate all runtime data about actors, including position,
 * velocity, {@link Attribute}s, and behavior components. They are the primary objects
 * that interact with the game's {@link me.arcademadness.omni_dungeon.environment.Environment}.
 * </p>
 */
public interface Entity {

    // --------------------
    // Primitives
    // --------------------

    /**
     * Returns the current velocity of the entity.
     *
     * @return a {@link Vector2} representing the entity's velocity
     */
    Vector2 getVelocity();

    /**
     * Sets the entity's velocity.
     *
     * @param velocity a {@link Vector2} representing the new velocity
     */
    void setVelocity(Vector2 velocity);

    // --------------------
    // Attributes
    // --------------------

    /**
     * Returns the entity's health attribute.
     *
     * @return the {@link Health} of the entity
     */
    Health getHealth();

    /**
     * Returns the entity's armor attribute.
     *
     * @return the {@link Armor} of the entity
     */
    Armor getArmor();

    /**
     * Returns the entity's mana attribute.
     *
     * @return the {@link Mana} of the entity
     */
    Mana getMana();

    /**
     * Returns the entity's action points attribute.
     *
     * @return the {@link ActionPoints} of the entity
     */
    ActionPoints getActionPoints();

    /**
     * Returns the entity's acceleration attribute.
     *
     * @return the {@link Acceleration} of the entity
     */
    Acceleration getAcceleration();

    /**
     * Returns the entity's friction attribute.
     *
     * @return the {@link Friction} of the entity
     */
    Friction getFriction();

    /**
     * Returns the entity's maximum speed attribute.
     *
     * @return the {@link MaxSpeed} of the entity
     */
    MaxSpeed getMaxSpeed();

    /**
     * Returns the entity's inventory component.
     *
     * @return the {@link Inventory} of the entity
     */
    Inventory getInventory();

    /**
     * Returns the entity's location component.
     *
     * @return the {@link Location} of the entity
     */
    Location getLocation();

    /**
     * Returns the root part of the entity's structure.
     *
     * @return the {@link EntityPart} representing the root of the entity
     */
    EntityPart getRootPart();

    /**
     * Sets the root part of the entity's structure.
     *
     * @param rootPart the {@link EntityPart} to set as root
     */
    void setRootPart(EntityPart rootPart);

    /**
     * Returns the entity's current view of the environment.
     *
     * @return the {@link EnvironmentView} associated with this entity
     */
    EnvironmentView getEnvironment();

    /**
     * Sets the entity's view of the environment.
     *
     * @param environment the {@link EnvironmentView} to associate with this entity
     */
    void setEnvironment(EnvironmentView environment);

    /**
     * Returns the controller responsible for this entity.
     *
     * @return the {@link Controller} controlling this entity
     */
    Controller getController();

    /**
     * Sets the controller responsible for this entity.
     *
     * @param controller the {@link Controller} to assign to this entity
     */
    void setController(Controller controller);

    /**
     * Returns the set of tile coordinates currently occupied by this entity.
     *
     * @return a {@link Set} of {@link TileCoordinate} representing the occupied tiles
     */
    Set<TileCoordinate> getOccupiedTiles();
}
