package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.events.entity.EntityDespawnEvent;
import me.arcademadness.omni_dungeon.events.entity.EntitySpawnEvent;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.services.CollisionService;
import me.arcademadness.omni_dungeon.environment.services.MovementService;
import me.arcademadness.omni_dungeon.events.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Environment implements EnvironmentControl {

    private final TileMap map;
    private final List<Entity> entities = new ArrayList<>();
    private final EventBus eventBus;

    private final CollisionService collisionService;
    private final MovementService movementService;

    public Environment(TileMap map) {
        this.map = map;
        this.collisionService = new CollisionService(this);
        this.movementService = new MovementService(this);
        this.eventBus = new EventBus();
    }

    @Override public TileMap getMap() { return map; }
    @Override public List<Entity> getEntities() { return Collections.unmodifiableList(entities); }
    @Override public CollisionService getCollisionService() { return collisionService; }
    @Override public MovementService getMovementService() { return movementService; }
    @Override public EventBus getEventBus() { return eventBus; }

    @Override
    public void spawn(Entity entity, Location location) {
        eventBus.post(new EntitySpawnEvent(entity, location, this));
    }

    @Override
    public void addEntity(Entity e) {
        entities.add(e);
        e.setEnvironment(this);
        collisionService.updateEntityPartsInTiles(e);
    }

    @Override
    public void despawn(Entity entity) {
        eventBus.post(new EntityDespawnEvent(entity, this));
    }


    @Override
    public void removeEntity(Entity entity) {
        if (!entities.remove(entity)) return;
        entity.setEnvironment(null);
        collisionService.updateEntityPartsInTiles(entity);
    }


    @Override
    public void tick(float delta) {
        List<Entity> snapshot = new ArrayList<>(entities);

        for (Entity entity : snapshot) {
            if (entity.getController() == null) continue;
            Optional<ControlIntent> opt = entity.getController().getIntent();
            if (!opt.isPresent()) continue;

            ControlIntent intent = opt.get();
            for (Action action : intent.getActions()) {
                if (action.canExecute(this, entity)) {
                    action.execute(this, entity, delta);
                }
            }
        }

        movementService.tick(delta);
    }
}
