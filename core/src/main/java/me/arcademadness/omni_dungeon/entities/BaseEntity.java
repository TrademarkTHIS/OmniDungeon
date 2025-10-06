package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.components.*;
import me.arcademadness.omni_dungeon.controllers.Controller;
import me.arcademadness.omni_dungeon.visuals.Visual;

public abstract class BaseEntity implements Entity {
    protected Health health;
    protected Armor armor;
    protected Mana mana;
    protected ActionPoints actionPoints;
    protected Inventory inventory;
    protected Equipment equipment;
    protected Location location;
    protected Controller controller;
    protected Visual visual;

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

    @Override
    public Bounds getBounds() {
        float x = location.getX() * TileMap.TILE_SIZE;
        float y = location.getY() * TileMap.TILE_SIZE;
        return new Bounds(x, y, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public Visual getVisual() {
        return visual;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

}

