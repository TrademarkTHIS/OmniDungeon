package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.attributes.Acceleration;
import me.arcademadness.omni_dungeon.attributes.Armor;
import me.arcademadness.omni_dungeon.attributes.Health;
import me.arcademadness.omni_dungeon.attributes.MaxSpeed;

public class PlayerEntity extends BaseEntity {
    public PlayerEntity(int x, int y) {
        super(x, y);
        this.health = new Health(150);
        this.armor = new Armor(5);
        this.inventory.addItem("Starter Sword");
        this.maxSpeed = new MaxSpeed(50);
        this.acceleration = new Acceleration(50);
    }
}

