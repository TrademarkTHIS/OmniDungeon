package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.*;
import me.arcademadness.omni_dungeon.controllers.Controller;

import java.util.List;
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
    // Primitives
    Vector2 getVelocity();
    void setVelocity(Vector2 velocity);

    // Attributes
    Health getHealth();
    Armor getArmor();
    Mana getMana();
    ActionPoints getActionPoints();

    Acceleration getAcceleration();
    Friction getFriction();
    MaxSpeed getMaxSpeed();

    // Components
    Inventory getInventory();
    Location getLocation();
    EntityPart getRootPart();
    void setRootPart(EntityPart rootPart);


    Controller getController();
    void setController(Controller controller);

    Set<TileCoordinate> getOccupiedTiles();
}
