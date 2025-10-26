package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.attributes.Acceleration;
import me.arcademadness.omni_dungeon.attributes.Health;
import me.arcademadness.omni_dungeon.attributes.MaxSpeed;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.controllers.BeeController;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;

import java.util.Random;

public class BeeEntity extends BaseEntity {
    private final BeeController controller = new BeeController();

    public BeeEntity() {
        super();
        Random r = new Random();
        int min = 75;
        int max = 125;
        this.maxSpeed = new MaxSpeed(r.nextInt(max - min + 1) + min);
        this.acceleration = new Acceleration(r.nextInt(max - min + 1) + min);
        this.health = new Health(1);

        int tileSize = EnvironmentConfig.get().getTileSize();

        ShapeVisual visual = new ShapeVisual(Color.YELLOW);
        Rectangle collider = new Rectangle(0, 0, tileSize/4f, tileSize/4f);
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
