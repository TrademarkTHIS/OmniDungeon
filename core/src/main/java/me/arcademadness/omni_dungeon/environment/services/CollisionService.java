package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.world.TileMap;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.components.TileCoordinate;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles entity–tile and entity–entity collision resolution.
 * <p>
 * This version is fully hierarchy-aware: every {@link EntityPart}, including nested
 * child parts, is considered in collision and tile occupancy.
 * </p>
 */
public class CollisionService {

    private final TileMap map;
    private static final float EPS = 0.001f;

    public CollisionService(Environment environment) {
        this.map = environment.getMap();
    }

    /**
     * Moves the entity along one axis and resolves collisions against tiles and other entities.
     *
     * @param entity         The entity being moved.
     * @param xTiles         The current X position in tiles.
     * @param yTiles         The current Y position in tiles.
     * @param deltaMoveTiles The attempted delta movement in tiles.
     * @param horizontal     True if moving horizontally, false if vertically.
     * @return The adjusted final position along that axis in tile coordinates.
     */
    public float moveAxis(Entity entity, float xTiles, float yTiles, float deltaMoveTiles, boolean horizontal) {
        float start = horizontal ? xTiles : yTiles;
        float proposed = start + deltaMoveTiles;
        final float[] adjustedResult = { proposed };

        entity.getRootPart().forEachPart(part -> {
            Rectangle collider = part.getCollider();
            if (collider == null) return;

            float baseWorldX = part.getWorldX();
            float baseWorldY = part.getWorldY();

            float partX = horizontal
                ? baseWorldX + (adjustedResult[0] - xTiles) * map.getTileSize()
                : baseWorldX;
            float partY = horizontal
                ? baseWorldY
                : baseWorldY + (adjustedResult[0] - yTiles) * map.getTileSize();

            Rectangle predicted = new Rectangle(partX, partY, collider.width, collider.height);

            float adjusted = adjustedResult[0];
            adjusted = resolveTileCollision(predicted, horizontal, start, adjusted, deltaMoveTiles);
            adjusted = resolveEntityCollision(entity, predicted, horizontal, start, adjusted);

            adjustedResult[0] = adjusted;
        });

        return adjustedResult[0];
    }

    private float resolveTileCollision(Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles, float deltaMoveTiles) {
        int startX = Math.max(0, Math.min((int) (predicted.x / map.getTileSize()), map.width - 1));
        int endX = Math.max(0, Math.min((int) ((predicted.x + predicted.width - EPS) / map.getTileSize()), map.width - 1));
        int startY = Math.max(0, Math.min((int) (predicted.y / map.getTileSize()), map.height - 1));
        int endY = Math.max(0, Math.min((int) ((predicted.y + predicted.height - EPS) / map.getTileSize()), map.height - 1));

        boolean movingPositive = deltaMoveTiles > 0f;

        if (horizontal) {
            if (movingPositive) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[endX][ty].walkable) {
                        return (endX * map.getTileSize() - predicted.width) / (float) map.getTileSize();
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[startX][ty].walkable) {
                        return ((startX + 1) * map.getTileSize()) / (float) map.getTileSize();
                    }
                }
            }
        } else {
            if (movingPositive) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][endY].walkable) {
                        return (endY * map.getTileSize() - predicted.height) / (float) map.getTileSize();
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][startY].walkable) {
                        return ((startY + 1) * map.getTileSize()) / (float) map.getTileSize();
                    }
                }
            }
        }
        return proposedTiles;
    }

    private float resolveEntityCollision(Entity selfEntity, Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles) {
        Set<EntityPart> nearbyParts = new HashSet<>();

        int startX = Math.max(0, Math.min((int) (predicted.x / map.getTileSize()), map.width - 1));
        int endX = Math.max(0, Math.min((int) ((predicted.x + predicted.width - EPS) / map.getTileSize()), map.width - 1));
        int startY = Math.max(0, Math.min((int) (predicted.y / map.getTileSize()), map.height - 1));
        int endY = Math.max(0, Math.min((int) ((predicted.y + predicted.height - EPS) / map.getTileSize()), map.height - 1));

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
            float otherX = otherPart.getWorldX();
            float otherY = otherPart.getWorldY();
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
            return stopX / map.getTileSize();
        } else {
            float stopY;
            if (self.y < other.y) {
                stopY = other.y - self.height;
            } else {
                stopY = other.y + other.height;
            }
            return stopY / map.getTileSize();
        }
    }

    public void updateEntityPartsInTiles(Entity entity) {
        Set<TileCoordinate> oldTiles = entity.getOccupiedTiles();
        Set<TileCoordinate> newTiles = new HashSet<>();

        entity.getRootPart().forEachPart(part -> {
            Rectangle collider = part.getCollider();
            if (collider == null) return;


            float worldX = part.getWorldX();
            float worldY = part.getWorldY();

            int startX = Math.max(0, (int) (worldX / map.getTileSize()));
            int endX   = Math.min(map.width - 1, (int) ((worldX + collider.width - EPS) / map.getTileSize()));
            int startY = Math.max(0, (int) (worldY / map.getTileSize()));
            int endY   = Math.min(map.height - 1, (int) ((worldY + collider.height - EPS) / map.getTileSize()));

            for (int tx = startX; tx <= endX; tx++) {
                for (int ty = startY; ty <= endY; ty++) {
                    TileCoordinate tc = new TileCoordinate(tx, ty);
                    newTiles.add(tc);
                    map.tiles[tx][ty].parts.add(part);
                }
            }
        });

        for (TileCoordinate old : oldTiles) {
            if (!newTiles.contains(old)
                && old.x >= 0 && old.x < map.width
                && old.y >= 0 && old.y < map.height) {
                map.tiles[old.x][old.y].parts.removeIf(p -> p.getOwner() == entity);
            }
        }

        oldTiles.clear();
        oldTiles.addAll(newTiles);
    }
}
