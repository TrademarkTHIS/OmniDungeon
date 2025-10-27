package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.Optional;

public interface Goal<T extends Entity> {
    /**
     * Lower number = higher priority. E.g., 0 = highest.
     */
    int getPriority();

    /**
     * Returns an optional ControlIntent if this goal wants the entity to act.
     */
    Optional<ControlIntent> computeIntent(T entity);
}
