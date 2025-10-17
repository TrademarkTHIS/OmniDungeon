package me.arcademadness.omni_dungeon.events.entity;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.entities.Entity;

public class EntityMoveEvent extends EntityEvent {
    private final World world;
    private Vector2 direction;
    private final float delta;

    public EntityMoveEvent(World world, Entity entity, Vector2 direction, float delta) {
        super(entity);
        this.direction = direction;
        this.world = world;
        this.delta = delta;
    }

    public Vector2 getDirection() { return direction; }
    public void setDirection(Vector2 direction) {
        this.direction=direction;
    }

    @Override
    public void execute() {
        float vx = entity.getVelocityX();
        float vy = entity.getVelocityY();

        float accel = entity.getAcceleration().getFinalValue();
        vx += direction.x * accel * delta;
        vy += direction.y * accel * delta;

        float friction = entity.getFriction().getFinalValue();
        vx *= (1 - friction * delta);
        vy *= (1 - friction * delta);

        float maxSpeed = entity.getMaxSpeed().getFinalValue();
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            float scale = maxSpeed / speed;
            vx *= scale;
            vy *= scale;
        }

        entity.setVelocity(vx, vy);

        float newX = entity.getLocation().getX() + vx * delta;
        float newY = entity.getLocation().getY() + vy * delta;

        float[] resolved = world.resolveCollisions(entity, newX, newY);
        entity.getLocation().set(resolved[0], resolved[1]);
    }
}

