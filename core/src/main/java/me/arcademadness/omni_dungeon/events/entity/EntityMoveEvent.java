package me.arcademadness.omni_dungeon.events.entity;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.services.MovementService;
import me.arcademadness.omni_dungeon.entities.Entity;

public class EntityMoveEvent extends EntityEvent {
    private final MovementService movement;
    private final Vector2 direction;

    public EntityMoveEvent(Environment environment, Entity entity, Vector2 direction) {
        super(entity);
        this.direction = direction;
        this.movement = environment.getMovementService();
    }

    @Override
    public void execute() {
        movement.move(entity, direction);
    }
}
