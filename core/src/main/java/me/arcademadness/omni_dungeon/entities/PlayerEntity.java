package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.items.Sword;

public class PlayerEntity extends BaseEntity {
    public PlayerEntity(int x, int y) {
        super(x, y);
        this.health = new Health(150);
        this.armor = new Armor(5);
        for (int i = 0; i < 550*10; i++) {
            this.inventory.addItem(new Sword());
        }
        this.maxSpeed = new MaxSpeed(50);
        this.acceleration = new Acceleration(50);
        this.friction = new Friction(10);
    }
}

