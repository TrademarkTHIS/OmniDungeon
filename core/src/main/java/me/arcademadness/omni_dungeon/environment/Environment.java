package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.controllers.Controller;
import me.arcademadness.omni_dungeon.events.entity.EntityDespawnEvent;
import me.arcademadness.omni_dungeon.events.entity.EntitySpawnEvent;
import me.arcademadness.omni_dungeon.environment.world.Floor;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.services.CollisionService;
import me.arcademadness.omni_dungeon.environment.services.MovementService;
import me.arcademadness.omni_dungeon.events.EventBus;
import me.arcademadness.omni_dungeon.util.StableList;

import java.util.Collections;
import java.util.List;

public class Environment implements EnvironmentControl {

    private long currentTick = 0;

    private final Floor map;
    private final StableList<Entity> entities = new StableList<>();
    private final EventBus eventBus;

    private final CollisionService collisionService;
    private final MovementService movementService;

    public Environment(Floor map) {
        this.map = map;
        this.collisionService = new CollisionService(this);
        this.movementService = new MovementService(this);
        this.eventBus = new EventBus();
    }

    @Override public Floor getMap() { return map; }
    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities.data());
    }
    @Override public CollisionService getCollisionService() { return collisionService; }
    @Override public MovementService getMovementService() { return movementService; }
    @Override public EventBus getEventBus() { return eventBus; }

    @Override
    public void spawn(Entity entity, Location location) {
        eventBus.post(new EntitySpawnEvent(entity, location, this));
    }

    @Override
    public void despawn(Entity entity) {
        eventBus.post(new EntityDespawnEvent(entity, this));
    }


    @Override
    public void addEntity(Entity e) {
        entities.add(e);
        e.setEnvironment(this);
        collisionService.updateEntityPartsInTiles(e);
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!entities.data().remove(entity)) return;
        entity.setEnvironment(null);
        collisionService.updateEntityPartsInTiles(entity);
    }

    @Override
    public void tick(float delta) {
        currentTick++;

        var data = entities.data();
        for (int i = data.size() - 1; i >= 0; i--) {
            Entity entity = data.get(i);

            Controller controller = entity.getController();
            if (controller == null) continue;

            controller.getIntent().ifPresent(intent -> {
                List<Action> actions = intent.getActions();
                for (Action action : actions) {
                    if (action.canExecute(this, entity)) {
                        action.execute(this, entity, delta);
                    }
                }
            });
        }

        movementService.tick(delta);
    }

    public long getCurrentTick() {
        return currentTick;
    }
}
