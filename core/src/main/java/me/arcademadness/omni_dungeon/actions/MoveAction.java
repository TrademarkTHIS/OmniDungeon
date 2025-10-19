package me.arcademadness.omni_dungeon.actions;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.events.entity.EntityMoveEvent;
import me.arcademadness.omni_dungeon.events.EventBus;

public class MoveAction implements Action {
    private final Vector2 direction;

    public MoveAction(Vector2 direction) {
        this.direction = direction;
    }

    @Override
    public boolean canExecute(Environment environment, Entity entity) {
        return true;
    }

    @Override
    public void execute(Environment environment, Entity entity, float delta) {
        EntityMoveEvent event = new EntityMoveEvent(environment, entity, direction, delta);
        EventBus.getInstance().post(event);
    }
}
