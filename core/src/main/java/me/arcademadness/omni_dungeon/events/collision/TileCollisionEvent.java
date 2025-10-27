package me.arcademadness.omni_dungeon.events.collision;

import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.Tile;

/**
 * Represents a collision between an entity part and a tile.
 */
public class TileCollisionEvent extends CollisionEvent {

    private final EntityPart colliderPart;
    private final Tile tile;

    public TileCollisionEvent(EntityPart colliderPart, Tile tile, EnvironmentView env) {
        super(env);
        this.colliderPart = colliderPart;
        this.tile = tile;
    }

    public EntityPart getColliderPart() {
        return colliderPart;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    protected void execute() {
        // Optional reaction logic
    }
}

