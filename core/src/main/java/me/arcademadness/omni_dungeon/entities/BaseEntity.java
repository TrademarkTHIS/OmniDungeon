package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.controllers.Controller;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.movement.MovementHelper;

public abstract class BaseEntity implements Entity {
    protected Health health;
    protected Armor armor;
    protected Mana mana;
    protected ActionPoints actionPoints;
    protected Inventory inventory;
    protected Equipment equipment;
    protected Location location;

    protected Controller controller;

    public BaseEntity(int startX, int startY) {
        this.location = new Location(startX, startY);

        this.health = new Health(100);
        this.armor = new Armor(0);
        this.mana = new Mana(50);
        this.actionPoints = new ActionPoints(10);
        this.inventory = new Inventory();
        this.equipment = new Equipment();
    }

    @Override public Health getHealth() { return health; }
    @Override public Armor getArmor() { return armor; }
    @Override public Mana getMana() { return mana; }
    @Override public ActionPoints getActionPoints() { return actionPoints; }
    @Override public Inventory getInventory() { return inventory; }
    @Override public Equipment getEquipment() { return equipment; }
    @Override public Location getLocation() { return location; }

    @Override
    public Controller getController() { return controller; }
    @Override
    public void setController(Controller controller) { this.controller = controller; }

    @Override public void render(ShapeRenderer shape) {}

    @Override
    public void update(float delta, World world) {
        if (controller != null) {
            controller.update(this, world, delta);
        }
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(location.getX(), location.getY(), MovementHelper.ENTITY_SIZE, MovementHelper.ENTITY_SIZE);
    }


}
