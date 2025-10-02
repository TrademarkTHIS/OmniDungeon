package me.arcademadness.omni_dungeon.movement;

import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.attributes.Bounds;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.attributes.Location;

import java.util.List;

public class MovementHelper {
    public static final float ENTITY_SIZE = 1f;

    public static void applyMovement(Entity entity, MovementIntent intent, float delta, World world) {
        if (intent == null) return;

        Location loc = entity.getLocation();
        TileMap map = world.getMap();
        List<Entity> others = world.getEntities();
        float newX = loc.getX() + intent.dx * delta;
        float newY = loc.getY() + intent.dy * delta;

        float finalX = loc.getX();
        if (intent.dx != 0) {
            if (intent.dx > 0) {
                int rightTile = (int)(newX + ENTITY_SIZE);
                int bottomTile = (int)loc.getY();
                int topTile = (int)(loc.getY() + ENTITY_SIZE - 0.001f);
                if (map.tiles[rightTile][bottomTile].walkable && map.tiles[rightTile][topTile].walkable) {
                    finalX = newX;
                } else {
                    finalX = rightTile - ENTITY_SIZE;
                }
            } else {
                int leftTile = (int)newX;
                int bottomTile = (int)loc.getY();
                int topTile = (int)(loc.getY() + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][bottomTile].walkable && map.tiles[leftTile][topTile].walkable) {
                    finalX = newX;
                } else {
                    finalX = leftTile + 1;
                }
            }
        }

        float finalY = loc.getY();
        if (intent.dy != 0) {
            if (intent.dy > 0) {
                int topTile = (int)(newY + ENTITY_SIZE);
                int leftTile = (int)finalX;
                int rightTile = (int)(finalX + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][topTile].walkable && map.tiles[rightTile][topTile].walkable) {
                    finalY = newY;
                } else {
                    finalY = topTile - ENTITY_SIZE;
                }
            } else {
                int bottomTile = (int)newY;
                int leftTile = (int)finalX;
                int rightTile = (int)(finalX + ENTITY_SIZE - 0.001f);
                if (map.tiles[leftTile][bottomTile].walkable && map.tiles[rightTile][bottomTile].walkable) {
                    finalY = newY;
                } else {
                    finalY = bottomTile + 1;
                }
            }
        }
        Bounds newBounds = new Bounds(finalX, finalY, ENTITY_SIZE, ENTITY_SIZE);

        for (Entity other : others) {
            if (other == entity) continue;
            if (newBounds.intersects(other.getBounds())) {
                if (intent.dx != 0) finalX = entity.getLocation().getX();
                if (intent.dy != 0) finalY = entity.getLocation().getY();
                break;
            }
        }


        loc.set(finalX, finalY);
    }
}
