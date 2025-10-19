package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.components.TileCoordinate;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;

import java.util.HashSet;
import java.util.Set;

public class CollisionSystem {

    private final TileMap map;
    private static final float EPS = 0.001f;

    public CollisionSystem(Environment environment) {
        this.map = environment.getMap();
    }

    public float moveAxis(Entity entity, float xTiles, float yTiles, float deltaMoveTiles, boolean horizontal) {
        float start = horizontal ? xTiles : yTiles;
        float proposed = start + deltaMoveTiles;

        for (EntityPart part : entity.getParts()) {
            Rectangle collider = part.getCollider();
            if (collider == null) continue;

            float partX = horizontal ? (proposed * TileMap.TILE_SIZE + part.getOffset().x) : (xTiles * TileMap.TILE_SIZE + part.getOffset().x);
            float partY = horizontal ? (yTiles * TileMap.TILE_SIZE + part.getOffset().y) : (proposed * TileMap.TILE_SIZE + part.getOffset().y);

            Rectangle predicted = new Rectangle(partX, partY, collider.width, collider.height);

            proposed = resolveTileCollision(predicted, horizontal, start, proposed, deltaMoveTiles);
            proposed = resolveEntityCollision(entity, predicted, horizontal, start, proposed);
        }

        return proposed;
    }

    private float resolveTileCollision(Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles, float deltaMoveTiles) {
        int startX = Math.max(0, Math.min((int) (predicted.x / TileMap.TILE_SIZE), map.width - 1));
        int endX = Math.max(0, Math.min((int) ((predicted.x + predicted.width - EPS) / TileMap.TILE_SIZE), map.width - 1));
        int startY = Math.max(0, Math.min((int) (predicted.y / TileMap.TILE_SIZE), map.height - 1));
        int endY = Math.max(0, Math.min((int) ((predicted.y + predicted.height - EPS) / TileMap.TILE_SIZE), map.height - 1));

        boolean movingPositive = deltaMoveTiles > 0f;

        if (horizontal) {
            if (movingPositive) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[endX][ty].walkable) {
                        return (endX * TileMap.TILE_SIZE - predicted.width) / (float) TileMap.TILE_SIZE;
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[startX][ty].walkable) {
                        return ((startX + 1) * TileMap.TILE_SIZE) / (float) TileMap.TILE_SIZE;
                    }
                }
            }
        } else {
            if (movingPositive) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][endY].walkable) {
                        return (endY * TileMap.TILE_SIZE - predicted.height) / (float) TileMap.TILE_SIZE;
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][startY].walkable) {
                        return ((startY + 1) * TileMap.TILE_SIZE) / (float) TileMap.TILE_SIZE;
                    }
                }
            }
        }
        return proposedTiles;
    }

    private float resolveEntityCollision(Entity selfEntity, Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles) {
        Set<EntityPart> nearbyParts = new HashSet<>();

        int startX = Math.max(0, Math.min((int) (predicted.x / TileMap.TILE_SIZE), map.width - 1));
        int endX = Math.max(0, Math.min((int) ((predicted.x + predicted.width - EPS) / TileMap.TILE_SIZE), map.width - 1));
        int startY = Math.max(0, Math.min((int) (predicted.y / TileMap.TILE_SIZE), map.height - 1));
        int endY = Math.max(0, Math.min((int) ((predicted.y + predicted.height - EPS) / TileMap.TILE_SIZE), map.height - 1));

        for (int tx = startX; tx <= endX; tx++) {
            for (int ty = startY; ty <= endY; ty++) {
                nearbyParts.addAll(map.tiles[tx][ty].parts);
            }
        }

        float adjusted = proposedTiles;

        for (EntityPart otherPart : nearbyParts) {
            if (otherPart.getOwner() == selfEntity) continue;

            Rectangle otherCollider = otherPart.getCollider();
            if (otherCollider == null) continue;

            Rectangle otherRect = new Rectangle(otherCollider);
            float otherX = otherPart.getWorldX() * TileMap.TILE_SIZE;
            float otherY = otherPart.getWorldY() * TileMap.TILE_SIZE;
            otherRect.setPosition(otherX, otherY);

            if (predicted.overlaps(otherRect)) {
                float stop = computeEntityCollisionStop(predicted, otherRect, horizontal);
                if (proposedTiles > startTiles) {
                    if (stop < adjusted) adjusted = stop;
                } else if (proposedTiles < startTiles) {
                    if (stop > adjusted) adjusted = stop;
                }
            }
        }

        return adjusted;
    }

    private float computeEntityCollisionStop(Rectangle self, Rectangle other, boolean horizontal) {
        if (horizontal) {
            float stopX;
            if (self.x < other.x) {
                stopX = other.x - self.width;
            } else {
                stopX = other.x + other.width;
            }
            return stopX / TileMap.TILE_SIZE;
        } else {
            float stopY;
            if (self.y < other.y) {
                stopY = other.y - self.height;
            } else {
                stopY = other.y + other.height;
            }
            return stopY / TileMap.TILE_SIZE;
        }
    }

    public void updateEntityPartsInTiles(Entity entity) {
        Set<TileCoordinate> oldTiles = entity.getOccupiedTiles();
        Set<TileCoordinate> newTiles = new HashSet<>();

        for (EntityPart part : entity.getParts()) {
            Rectangle collider = part.getCollider();
            if (collider == null) continue;

            float worldX = entity.getLocation().x * TileMap.TILE_SIZE + part.getOffset().x;
            float worldY = entity.getLocation().y * TileMap.TILE_SIZE + part.getOffset().y;

            int startX = Math.max(0, (int) (worldX / TileMap.TILE_SIZE));
            int endX = Math.min(map.width - 1, (int) ((worldX + collider.width - EPS) / TileMap.TILE_SIZE));
            int startY = Math.max(0, (int) (worldY / TileMap.TILE_SIZE));
            int endY = Math.min(map.height - 1, (int) ((worldY + collider.height - EPS) / TileMap.TILE_SIZE));

            for (int tx = startX; tx <= endX; tx++) {
                for (int ty = startY; ty <= endY; ty++) {
                    TileCoordinate tc = new TileCoordinate(tx, ty);
                    newTiles.add(tc);
                    map.tiles[tx][ty].parts.add(part);
                }
            }
        }

        for (TileCoordinate old : oldTiles) {
            if (!newTiles.contains(old)) {
                if (old.x >= 0 && old.x < map.width && old.y >= 0 && old.y < map.height) {
                    map.tiles[old.x][old.y].parts.removeIf(p -> p.getOwner() == entity);
                }
            }
        }

        oldTiles.clear();
        oldTiles.addAll(newTiles);
    }
}

