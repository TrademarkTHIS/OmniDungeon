package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.controllers.goals.RandomWanderGoal;
import me.arcademadness.omni_dungeon.entities.Entity;

public class MobController extends GoalController<Entity> {

    public MobController() {
        // Random wandering with priority 5, changing direction every 2 seconds
        addGoal(new RandomWanderGoal<>(5));
    }
}
