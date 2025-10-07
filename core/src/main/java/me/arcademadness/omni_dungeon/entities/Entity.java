package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.*;
import me.arcademadness.omni_dungeon.controllers.Controller;
import me.arcademadness.omni_dungeon.visuals.Visual;

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
    Location getLocation();
    Bounds getBounds();
    Visual getVisual();

    Controller getController();
    void setController(Controller controller);
}
