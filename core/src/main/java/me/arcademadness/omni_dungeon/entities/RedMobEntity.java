package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.attributes.*;
import me.arcademadness.omni_dungeon.controllers.MobController;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;

public class RedMobEntity extends MobEntity {

    public RedMobEntity() {
        super();
        this.maxSpeed = new MaxSpeed(10);
        this.acceleration = new Acceleration(20);
        this.friction = new Friction(maxSpeed, 2f);

        ShapeVisual visual = new ShapeVisual(Color.RED);
        Rectangle collider = new Rectangle(0, 0, 0.75f, 0.75f);
        EntityPart part = new EntityPart(this, visual, collider);

        setRootPart(part);

        setController(new MobController());
    }
}
