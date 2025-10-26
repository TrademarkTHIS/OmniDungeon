package me.arcademadness.omni_dungeon.events;

import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;

/**
 * Represents a collision between two entity parts.
 * The reaction to the collision is handled by systems listening for this event.
 */
public class PartCollisionEvent extends BaseEvent {

    /** The first part involved in the collision (the moving part). */
    private final EntityPart colliderPart;

    /** The second part involved in the collision (the part being collided with). */
    private final EntityPart collideePart;

    private final EnvironmentView env;

    public PartCollisionEvent(EntityPart colliderPart, EntityPart collideePart, EnvironmentView env) {
        this.colliderPart = colliderPart;
        this.collideePart = collideePart;
        this.env = env;
    }

    public EntityPart getColliderPart() {
        return colliderPart;
    }

    public EntityPart getCollideePart() {
        return collideePart;
    }

    public EnvironmentView getEnvironment() {
        return env;
    }

    protected void execute() {
        // Optional: define default collision reaction if desired
    }
}
