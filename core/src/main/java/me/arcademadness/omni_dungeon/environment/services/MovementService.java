package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

public class MovementService {
    private final EnvironmentView environment;
    private final CollisionService collision;

    public MovementService(EnvironmentView environment) {
        this.environment = environment;
        this.collision = environment.getCollisionService();
    }

    public void move(Entity entity, Vector2 direction, float delta) {
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

        float lerpFactor = 0.8f;
        entity.getLocation().x = lerp(entity.getLocation().x, newX, lerpFactor);
        entity.getLocation().y = lerp(entity.getLocation().y, newY, lerpFactor);

        collision.updateEntityPartsInTiles(entity);
    }

    private float lerp(float current, float target, float alpha) {
        return current + (target - current) * alpha;
    }
}
