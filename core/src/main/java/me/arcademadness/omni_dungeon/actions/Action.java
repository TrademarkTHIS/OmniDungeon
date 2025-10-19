package me.arcademadness.omni_dungeon.actions;

import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.entities.Entity;

public interface Action {
    boolean canExecute(Environment environment, Entity entity);
    void execute(Environment environment, Entity entity, float delta);
}
