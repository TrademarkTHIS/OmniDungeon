package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.controllers.goals.ChaseClosestEntityGoal;
import me.arcademadness.omni_dungeon.controllers.goals.RandomWanderGoal;
import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.events.EventListener;
import me.arcademadness.omni_dungeon.events.Subscribe;
import me.arcademadness.omni_dungeon.events.collision.PartCollisionEvent;
import me.arcademadness.omni_dungeon.events.entity.BeeStingEvent;

public class BeeController extends GoalController<BeeEntity> implements EventListener {

    public BeeController() {
        addGoal(new ChaseClosestEntityGoal<>(0, 50));
        addGoal(new RandomWanderGoal<>(5));
    }

    /**
     * Collision handling stays the same. Bees sting non-bees.
     */
    @Subscribe
    public void onCollision(PartCollisionEvent event) {
        if (event.getColliderPart().getOwner() != getEntity()) return;

        BeeEntity bee = getEntity();
        Entity target = event.getCollideePart().getOwner();
        if (target instanceof BeeEntity) return;

        BeeStingEvent beeSting = new BeeStingEvent(target, bee, 1);
        if (bee.getEnvironment() != null) {
            bee.getEnvironment().getEventBus().post(beeSting);
        }
    }
}
