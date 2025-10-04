package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.Bounds;
import me.arcademadness.omni_dungeon.components.Equipment;
import me.arcademadness.omni_dungeon.components.Inventory;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.controllers.Controller;

public abstract class BaseEntity implements Entity {
    protected Health health;
    protected Armor armor;
    protected Mana mana;
    protected ActionPoints actionPoints;
    protected Inventory inventory;
    protected Equipment equipment;
    protected Location location;
    protected Controller controller;

    protected float velocityX = 0;
    protected float velocityY = 0;

    protected Acceleration acceleration = new Acceleration(10);
    protected Friction friction = new Friction(8);
    protected MaxSpeed maxSpeed = new MaxSpeed(5);

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public Armor getArmor() {
        return armor;
    }

    @Override
    public Mana getMana() {
        return mana;
    }

    @Override
    public ActionPoints getActionPoints() {
        return actionPoints;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Equipment getEquipment() {
        return equipment;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public BaseEntity(int startX, int startY) {
        this.location = new Location(startX, startY);
        this.health = new Health(100);
        this.armor = new Armor(0);
        this.mana = new Mana(50);
        this.actionPoints = new ActionPoints(10);
        this.inventory = new Inventory();
        this.equipment = new Equipment();
    }

    @Override
    public float getVelocityX() {
        return velocityX;
    }

    @Override
    public float getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocity(float vx, float vy) {
        this.velocityX = vx; this.velocityY = vy;
    }

    @Override
    public Acceleration getAcceleration() {
        return acceleration;
    }

    @Override
    public Friction getFriction() {
        return friction;
    }

    @Override
    public MaxSpeed getMaxSpeed() {
        return maxSpeed;
    }

    @Override public void update(float delta, World world) {
        if (controller != null) {
            controller.update(this, world, delta);
        }
    }

    @Override public Bounds getBounds() {
        return new Bounds(location.getX(), location.getY(), World.ENTITY_SIZE, World.ENTITY_SIZE);
    }
}

