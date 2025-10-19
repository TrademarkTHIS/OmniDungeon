package me.arcademadness.omni_dungeon;

import me.arcademadness.omni_dungeon.components.EntityPart;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    public boolean walkable;
    public Tile(boolean walkable) {
        this.walkable = walkable;
    }
    public Set<EntityPart> parts = new HashSet<>();
}
