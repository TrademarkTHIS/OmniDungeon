package me.arcademadness.omni_dungeon.events.collision;

import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

/**
 * Represents a collision between two entity parts.
 */
public class PartCollisionEvent extends CollisionEvent {

    private final EntityPart colliderPart;
    private final EntityPart collideePart;

    public PartCollisionEvent(EntityPart colliderPart, EntityPart collideePart, EnvironmentView env) {
        super(env);
        this.colliderPart = colliderPart;
        this.collideePart = collideePart;
    }

    public EntityPart getColliderPart() {
        return colliderPart;
    }

    public EntityPart getCollideePart() {
        return collideePart;
    }

    @Override
    protected void execute() {

    }
}
