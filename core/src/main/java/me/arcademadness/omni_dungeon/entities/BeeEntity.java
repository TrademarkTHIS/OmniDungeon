package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.attributes.Acceleration;
import me.arcademadness.omni_dungeon.attributes.Friction;
import me.arcademadness.omni_dungeon.attributes.Health;
import me.arcademadness.omni_dungeon.attributes.MaxSpeed;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.controllers.BeeController;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;

import java.util.Random;

public class BeeEntity extends BaseEntity {
    private final BeeController controller = new BeeController();

    public BeeEntity() {
        super();
        Random r = new Random();
        int min = 10;
        int max = 20;
        int speed = r.nextInt(max - min + 1) + min;
        int accel = r.nextInt(max - min + 1) + min;
        this.maxSpeed = new MaxSpeed(speed);
        this.acceleration = new Acceleration(accel);
        this.friction = new Friction(maxSpeed, 0.1f);
        this.health = new Health(1);

        ShapeVisual visual = new ShapeVisual(Color.YELLOW);
        Rectangle collider = new Rectangle(0, 0, 0.25f, 0.25f);
        EntityPart part = new EntityPart(this, visual, collider);

        setRootPart(part);

        setController(controller);
    }

    @Override
    public void setEnvironment(EnvironmentView environment) {
        EnvironmentView oldEnv = getEnvironment();
        if (oldEnv != null && oldEnv != environment) {
            oldEnv.getEventBus().unregister(controller);
        }

        super.setEnvironment(environment);

        if (environment != null) {
            environment.getEventBus().register(controller);
        }
    }
}
