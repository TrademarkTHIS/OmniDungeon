package me.arcademadness.omni_dungeon.actions;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.events.entity.EntityMoveEvent;
import me.arcademadness.omni_dungeon.events.EventBus;


/**
 * An {@link Action} that moves an entity in a given direction.
 * <p>
 * When executed, this action posts an {@link EntityMoveEvent} to the {@link EventBus},
 * allowing systems to handle movement and collisions.
 * </p>
 *
 * @see EntityMoveEvent
 * @see EventBus
 */
public class MoveAction implements Action {
    private final Vector2 direction;

    public MoveAction(Vector2 direction) {
        this.direction = direction;
    }

    @Override
    public boolean canExecute(Environment environment, Entity entity) {
        return true;
    }

    /**
     * Executes the movement action by posting an {@link EntityMoveEvent}.
     *
     * @param environment the environment in which the entity moves
     * @param entity the entity that is moving
     * @param delta the frame delta time
     */
    @Override
    public void execute(Environment environment, Entity entity, float delta) {
        EntityMoveEvent event = new EntityMoveEvent(environment, entity, direction);
        environment.getEventBus().post(event);
    }
}
