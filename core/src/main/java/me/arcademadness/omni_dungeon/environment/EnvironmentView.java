package me.arcademadness.omni_dungeon.environment;

import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.services.CollisionService;
import me.arcademadness.omni_dungeon.environment.services.MovementService;
import me.arcademadness.omni_dungeon.events.EventBus;

import java.util.List;

public interface EnvironmentView {
    TileMap getMap();
    List<Entity> getEntities();
    CollisionService getCollisionService();
    MovementService getMovementService();
    EventBus getEventBus();
    void spawn(Entity entity, Location location);
    void despawn(Entity entity);
}
