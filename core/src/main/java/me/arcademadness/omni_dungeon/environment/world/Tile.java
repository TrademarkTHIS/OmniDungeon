package me.arcademadness.omni_dungeon.environment.world;

import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    public boolean walkable;
    public Set<EntityPart> parts = new HashSet<>();

    public Tile(boolean walkable) {
        this.walkable = walkable;
    }

    /** Add a part to this tile */
    public void addPart(EntityPart part) {
        if (part != null) parts.add(part);
    }

    /** Remove a part owned by a specific entity */
    public void removePartsOwnedBy(Entity entity) {
        parts.removeIf(p -> p.getOwner() == entity);
    }

    /** Remove all parts */
    public void clearParts() {
        parts.clear();
    }

    /** Get a copy of the parts currently in this tile */
    public Set<EntityPart> getPartsSnapshot() {
        return new HashSet<>(parts);
    }

    /** Check for if this tile is walkable */
    public boolean isWalkable() {
        return walkable;
    }
}
