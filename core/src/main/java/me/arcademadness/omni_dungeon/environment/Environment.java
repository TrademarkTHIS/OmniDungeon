package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.services.CollisionSystem;
import me.arcademadness.omni_dungeon.events.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Environment {
    private final TileMap map;
    private final List<Entity> entities = new ArrayList<>();
    private final EventBus eventBus;

    private final CollisionSystem collisionSystem;

    public Environment(TileMap map) {
        this.map = map;
        this.collisionSystem = new CollisionSystem(this);
        this.eventBus = new EventBus();
    }

    public TileMap getMap() { return map; }
    public List<Entity> getEntities() { return entities; }
    public CollisionSystem getCollisionSystem() { return collisionSystem; }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void addEntity(Entity e) {
        entities.add(e);
        collisionSystem.updateEntityPartsInTiles(e);
    }

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
    }
}
