package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.Bounds;
import me.arcademadness.omni_dungeon.components.Equipment;
import me.arcademadness.omni_dungeon.components.Inventory;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.controllers.Controller;

public interface Entity {
    // Primitives
    float getVelocityX();
    float getVelocityY();
    void setVelocity(float x, float y);

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
    Equipment getEquipment();
    Location getLocation();
    Bounds getBounds();

    Controller getController();
    void setController(Controller controller);

    void render(com.badlogic.gdx.graphics.glutils.ShapeRenderer shape);
    void update(float delta, World world);
}
