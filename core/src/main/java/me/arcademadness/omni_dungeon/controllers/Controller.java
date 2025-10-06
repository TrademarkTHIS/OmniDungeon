package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.entities.Entity;

public interface Controller {
    ControlIntent getIntent(Entity entity);

    default void update(Entity entity, World world, float delta) {
        ControlIntent intent = getIntent(entity);
        world.moveEntity(entity, intent, delta);
    }
}
