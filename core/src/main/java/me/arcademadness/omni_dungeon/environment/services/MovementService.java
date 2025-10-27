package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MovementService {
    private final EnvironmentView environment;
    private final CollisionService collision;

    // Stores the movement requests per entity
    private final Map<Entity, Vector2> movementQueue = new HashMap<>();

    public MovementService(EnvironmentView environment) {
        this.environment = environment;
        this.collision = environment.getCollisionService();
    }

    /**
     * Queue a movement request for this entity.
     * If already queued, it will override the previous request.
     */
    public void move(Entity entity, Vector2 direction) {
        if (direction == null || direction.isZero()) {
            return;
        }

        Vector2 impulse = direction.cpy();

        Vector2 vel = entity.getVelocity();
        vel.add(impulse);
        entity.setVelocity(vel);

        movementQueue.put(entity, new Vector2(0f, 0f));
    }

    /**
     * Process movement for all entities every game tick.
     */
    public void tick(float delta) {
        Iterator<Map.Entry<Entity, Vector2>> it = movementQueue.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Entity, Vector2> entry = it.next();
            Entity entity = entry.getKey();
            Vector2 request = entry.getValue();

            Vector2 velocity = entity.getVelocity();
            float friction = entity.getFriction().getFinalValue();      // units/sec
            float accel = entity.getAcceleration().getFinalValue();     // units/sec²
            float maxSpeed = entity.getMaxSpeed().getFinalValue();      // units/sec

            // Apply acceleration if there is input
            if (!request.isZero()) {
                Vector2 normDir = request.cpy().nor();
                velocity.x += normDir.x * accel * delta;
                velocity.y += normDir.y * accel * delta;
            }

            // Apply friction (linear deceleration)
            if (velocity.len2() > 0f) {
                float decelAmount = friction * delta;

                if (velocity.len() <= decelAmount) {
                    velocity.setZero();
                } else {
                    // Reduce velocity proportionally
                    velocity.scl((velocity.len() - decelAmount) / velocity.len());
                }
            }

            if (velocity.len() > maxSpeed) {
                float excess = velocity.len() - maxSpeed;
                float decel = Math.min(excess, accel);
                velocity.setLength(velocity.len() - decel);
            }


            entity.setVelocity(velocity);

            // Move entity with collision
            Vector2 newPos = collision.move(
                entity,
                entity.getLocation().x,
                entity.getLocation().y,
                velocity.x * delta,
                velocity.y * delta
            );
            entity.getLocation().set(newPos);
            collision.updateEntityPartsInTiles(entity);

            // Remove from queue if no movement and stopped
            if (velocity.isZero() && request.isZero()) {
                it.remove();
            } else {
                // Reset request so acceleration only applies once per input
                entry.setValue(new Vector2(0f, 0f));
            }
        }
    }

    private static class MovementRequest {
        final Vector2 direction;

        MovementRequest(Vector2 direction) {
            this.direction = direction;
        }
    }
}
