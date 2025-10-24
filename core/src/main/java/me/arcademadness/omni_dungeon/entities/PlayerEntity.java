package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.items.Sword;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;
import me.arcademadness.omni_dungeon.attributes.*;

public class PlayerEntity extends BaseEntity {

    public PlayerEntity(int x, int y) {
        super(x, y);

        this.health = new Health(150);
        this.armor = new Armor(5);
        this.maxSpeed = new MaxSpeed(50);
        this.acceleration = new Acceleration(50);
        this.friction = new Friction(10);

        int tileSize = EnvironmentConfig.get().getTileSize();

        ShapeVisual bodyVisual = new ShapeVisual(Color.CYAN);
        Rectangle bodyCollider = new Rectangle(0, 0, tileSize, tileSize);
        EntityPart bodyPart = new EntityPart(this, bodyVisual, bodyCollider);

        this.getParts().add(bodyPart);

        for (int i = 0; i < 7; i++) {
            this.inventory.addItem(new Sword());
        }
    }
}

