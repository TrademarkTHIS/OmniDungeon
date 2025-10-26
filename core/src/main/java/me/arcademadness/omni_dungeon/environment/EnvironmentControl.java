package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.entities.Entity;

public interface EnvironmentControl extends EnvironmentView {
    void tick(float delta);
    void addEntity(Entity e);
    void removeEntity(Entity e);
}
