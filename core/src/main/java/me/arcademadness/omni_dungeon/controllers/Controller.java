package me.arcademadness.omni_dungeon.controllers;

import java.util.Optional;

/**
 * Represents a source of control input for an entity or system.
 * <p>
 * Controllers provide {@link ControlIntent} objects that describe
 * intended actions or commands, which can be consumed by entities
 * or systems to drive behavior.
 * </p>
 */
public interface Controller {

    /**
     * Retrieves the current control intent, if one is available.
     * <p>
     * The returned {@link Optional} may be empty if there is no intent
     * to process at the current moment.
     * </p>
     *
     * @return an {@link Optional} containing the current {@link ControlIntent},
     *         or empty if no intent is present
     */
    Optional<ControlIntent> getIntent();
}
