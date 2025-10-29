package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.controllers.goals.*;
import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.events.EventListener;
import me.arcademadness.omni_dungeon.events.Subscribe;
import me.arcademadness.omni_dungeon.events.collision.PartCollisionEvent;
import me.arcademadness.omni_dungeon.events.entity.BeeStingEvent;

public class BeeController extends GoalController<BeeEntity> implements EventListener {

    public BeeController() {
        addGoal(new FindGroupGoal<>(0, 8));
        addGoal(new CreateGroupGoal<>(1, 100));
        addGoal(new MergeGroupGoal<>(2, 8));

        //Removed in favor of GroupFindTarget which is automatically added to the queen.
        //addGoal(new FindTargetGoal<>(3, 8));.
        addGoal(new GroupChaseGoal<>(4));
        addGoal(new GroupWanderGoal<>(5));

        addGoal(new RandomWanderGoal<>(6));
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
