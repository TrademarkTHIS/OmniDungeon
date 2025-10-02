package me.arcademadness.omni_dungeon;

import me.arcademadness.omni_dungeon.entities.Entity;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final TileMap map;
    private List<Entity> entities;

    public World(TileMap map) {
        this.map = map;
        this.entities = new ArrayList<>();
    }

    public TileMap getMap() {
        return map;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }
}
