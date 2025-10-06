package me.arcademadness.omni_dungeon.actions;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.entities.Entity;

public interface Action {
    boolean canExecute(World world, Entity entity);
    void execute(World world, Entity entity, float delta);
}
