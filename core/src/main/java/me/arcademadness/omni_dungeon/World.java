package me.arcademadness.omni_dungeon;

import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final TileMap map;
    private final List<Entity> entities;

    public static final float ENTITY_SIZE = 1f;

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

    public void tick(float delta) {
        for (Entity entity : entities) {
            ControlIntent intent = entity.getController().getIntent();
            if (intent == null || intent.isEmpty()) continue;

            for (Action action : intent.getActions()) {
                if (action.canExecute(this, entity)) {
                    action.execute(this, entity, delta);
                }
            }
        }
    }

    public float[] resolveCollisions(Entity entity, float newX, float newY) {
        float finalX = entity.getLocation().getX();
        float finalY = entity.getLocation().getY();

        if (newX != finalX) {
            if (newX > finalX) {
                int rightTile = (int) (newX + ENTITY_SIZE);
                int bottomTile = (int) finalY;
                int topTile = (int) (finalY + ENTITY_SIZE - 0.001f);
                if (map.tiles[rightTile][bottomTile].walkable && map.tiles[rightTile][topTile].walkable) {
                    finalX = newX;
                } else {
                    finalX = rightTile - ENTITY_SIZE;
                }
            } else {
                int leftTile = (int) newX;
                int bottomTile = (int) finalY;
                int topTile = (int) (finalY + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][bottomTile].walkable && map.tiles[leftTile][topTile].walkable) {
                    finalX = newX;
                } else {
                    finalX = leftTile + 1;
                }
            }
        }

        if (newY != finalY) {
            if (newY > finalY) {
                int topTile = (int) (newY + ENTITY_SIZE);
                int leftTile = (int) finalX;
                int rightTile = (int) (finalX + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][topTile].walkable && map.tiles[rightTile][topTile].walkable) {
                    finalY = newY;
                } else {
                    finalY = topTile - ENTITY_SIZE;
                }
            } else {
                int bottomTile = (int) newY;
                int leftTile = (int) finalX;
                int rightTile = (int) (finalX + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][bottomTile].walkable && map.tiles[rightTile][bottomTile].walkable) {
                    finalY = newY;
                } else {
                    finalY = bottomTile + 1;
                }
            }
        }

        float entLeft = finalX;
        float entRight = finalX + ENTITY_SIZE;
        float entBottom = finalY;
        float entTop = finalY + ENTITY_SIZE;

        for (Entity other : entities) {
            if (other == entity) continue;
            float ox = other.getLocation().getX();
            float oy = other.getLocation().getY();
            float oRight = ox + ENTITY_SIZE;
            float oTop = oy + ENTITY_SIZE;

            boolean overlap = entRight > ox && entLeft < oRight && entTop > oy && entBottom < oTop;
            if (overlap) {
                finalX = entity.getLocation().getX();
                finalY = entity.getLocation().getY();
                break;
            }
        }

        return new float[]{finalX, finalY};
    }

}
