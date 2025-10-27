package me.arcademadness.omni_dungeon.events.collision;

import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.events.BaseEvent;

/**
 * Base event for all types of collisions in the environment.
 */
public abstract class CollisionEvent extends BaseEvent {

    private final EnvironmentView env;

    public CollisionEvent(EnvironmentView env) {
        this.env = env;
    }

    public EnvironmentView getEnvironment() {
        return env;
    }
}

