package me.arcademadness.omni_dungeon.events;

/**
 * Represents a cancellable event in the game system.
 * <p>
 * Events are objects that can be posted to an {@link EventBus} and handled
 * by {@link EventListener}s. Certain events can be cancelled to prevent
 * default behavior or propagation.
 * </p>
 */
public interface Event {

    /**
     * Cancels this event.
     * <p>
     * Calling this method sets the event's canceled state to {@code true}.
     * </p>
     * @see #isCanceled()
     * @see #setCanceled(boolean)
     */
    void cancel();

    /**
     * Checks whether this event has been canceled.
     *
     * @return {@code true} if the event is canceled, {@code false} otherwise
     * @see #cancel()
     * @see #setCanceled(boolean)
     */
    boolean isCanceled();

    /**
     * Sets the canceled state of this event.
     *
     * @param canceled {@code true} to cancel the event, {@code false} to uncancel
     * @see #cancel()
     * @see #isCanceled()
     */
    void setCanceled(boolean canceled);
}
