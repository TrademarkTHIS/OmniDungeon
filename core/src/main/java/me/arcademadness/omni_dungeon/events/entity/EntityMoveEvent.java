package me.arcademadness.omni_dungeon.events.entity;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.CollisionSystem;
import me.arcademadness.omni_dungeon.entities.Entity;

public class EntityMoveEvent extends EntityEvent {
    private final CollisionSystem collision;
    private final Vector2 direction;
    private final float delta;

    public EntityMoveEvent(Environment environment, Entity entity, Vector2 direction, float delta) {
        super(entity);
        this.direction = direction;
        this.delta = delta;
        this.collision = new CollisionSystem(environment);
    }

    @Override
    public void execute() {
        float accel = entity.getAcceleration().getFinalValue();
        float vx = entity.getVelocity().x + direction.x * accel * delta;
        float vy = entity.getVelocity().y + direction.y * accel * delta;

        float friction = entity.getFriction().getFinalValue();
        vx *= (1 - friction * delta);
        vy *= (1 - friction * delta);

        float maxSpeed = entity.getMaxSpeed().getFinalValue();
        float speedSq = vx * vx + vy * vy;
        float maxSpeedSq = maxSpeed * maxSpeed;
        if (speedSq > maxSpeedSq) {
            float scale = maxSpeed / (float) Math.sqrt(speedSq);
            vx *= scale;
            vy *= scale;
        }

        entity.setVelocity(new Vector2(vx, vy));

        float newX = collision.moveAxis(entity, entity.getLocation().x, entity.getLocation().y, vx * delta, true);
        float newY = collision.moveAxis(entity, newX, entity.getLocation().y, vy * delta, false);

        entity.getLocation().set(newX, newY);

        collision.updateEntityPartsInTiles(entity);
    }
}
