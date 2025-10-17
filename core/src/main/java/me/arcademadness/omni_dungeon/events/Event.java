package me.arcademadness.omni_dungeon.events;

public interface Event {
    void cancel();
    boolean isCanceled();
}
