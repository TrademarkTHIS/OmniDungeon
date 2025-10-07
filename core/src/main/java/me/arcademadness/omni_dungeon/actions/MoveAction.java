package me.arcademadness.omni_dungeon.actions;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.entities.Entity;

public class MoveAction implements Action {
    private final float dx;
    private final float dy;

    public MoveAction(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public boolean canExecute(World world, Entity entity) {
        return true;
    }

    @Override
    public void execute(World world, Entity entity, float delta) {
        updateVelocity(entity, delta);
        moveAndResolveCollisions(world, entity, delta);
    }

    private void updateVelocity(Entity entity, float delta) {
        float vx = entity.getVelocityX();
        float vy = entity.getVelocityY();

        float accel = entity.getAcceleration().getFinalValue();
        vx += dx * accel * delta;
        vy += dy * accel * delta;

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
    }

    private void moveAndResolveCollisions(World world, Entity entity, float delta) {
        float newX = entity.getLocation().getX() + entity.getVelocityX() * delta;
        float newY = entity.getLocation().getY() + entity.getVelocityY() * delta;

        float[] resolved = world.resolveCollisions(entity, newX, newY);
        entity.getLocation().set(resolved[0], resolved[1]);
    }
}
