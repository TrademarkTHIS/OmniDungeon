package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.movement.MovementIntent;

public interface Controller {
    MovementIntent getIntent(Entity entity);

    default void update(Entity entity, World world, float delta) {
        MovementIntent intent = getIntent(entity);
        world.moveEntity(entity, intent, delta);
    }
}
