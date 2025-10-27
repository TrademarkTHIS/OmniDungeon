package me.arcademadness.omni_dungeon.events.entity;

import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;

public class EntitySpawnEvent extends EntityEvent {

    private final Environment env;
    private final Location location;

    public EntitySpawnEvent(Entity entity, Location location, Environment env) {
        super(entity);
        this.env = env;
        this.location = location;
    }

    @Override
    protected void execute() {
        entity.getLocation().set(location);
        env.addEntity(entity);
        env.getCollisionService().updateEntityPartsInTiles(entity);
        env.getCollisionService().checkImmediateCollisions(entity);
    }
}
