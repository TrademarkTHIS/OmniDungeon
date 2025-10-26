package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.components.TileCoordinate;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.events.Event;
import me.arcademadness.omni_dungeon.events.PartCollisionEvent;

import java.util.*;

/**
 * Handles entity–tile and entity–entity collision resolution.
 * <p>
 * This version is fully hierarchy-aware: every {@link EntityPart}, including nested
 * child parts, is considered in collision and tile occupancy.
 * </p>
 */
public class CollisionService {

    private final TileMap map;
    private final EnvironmentView env;
    private static final float EPS = 0.001f;

    public CollisionService(Environment environment) {
        this.env = environment;
        this.map = env.getMap();
    }

    /**
     * Moves an entity by deltaX and deltaY in tiles, resolving collisions against tiles and other entities.
     *
     * @param entity The entity being moved.
     * @param xTiles Current X in tiles.
     * @param yTiles Current Y in tiles.
     * @param deltaX Delta X in tiles.
     * @param deltaY Delta Y in tiles.
     * @return New position in tiles as a Vector2.
     */
    public Vector2 move(Entity entity, float xTiles, float yTiles, float deltaX, float deltaY) {
        float newX = xTiles;
        float newY = yTiles;

        newX = moveAxis(entity, newX, newY, deltaX, true);

        newY = moveAxis(entity, newX, newY, deltaY, false);

        if (deltaX != 0 && newX == xTiles) {
            newY = moveAxis(entity, newX, newY, deltaY, false);
        }
        if (deltaY != 0 && newY == yTiles) {
            newX = moveAxis(entity, newX, newY, deltaX, true);
        }

        return new Vector2(newX, newY);
    }

    float moveAxis(Entity entity, float xTiles, float yTiles, float deltaMoveTiles, boolean horizontal) {
        float start = horizontal ? xTiles : yTiles;
        float proposed = start + deltaMoveTiles;
        final float[] adjustedResult = { proposed };

        entity.getRootPart().forEachPart(part -> {
            Rectangle collider = part.getCollider();
            if (collider == null) return;

            float baseWorldX = part.getWorldX();
            float baseWorldY = part.getWorldY();

            float partX = horizontal ? baseWorldX + (adjustedResult[0] - xTiles) * map.getTileSize() : baseWorldX;
            float partY = horizontal ? baseWorldY : baseWorldY + (adjustedResult[0] - yTiles) * map.getTileSize();

            Rectangle predicted = new Rectangle(partX, partY, collider.width, collider.height);

            float adjusted = adjustedResult[0];
            adjusted = resolveTileCollision(predicted, horizontal, adjusted, deltaMoveTiles);
            adjusted = resolveEntityCollision(part, predicted, horizontal, start, adjusted);

            adjustedResult[0] = adjusted;
        });

        return adjustedResult[0];
    }

    private float resolveTileCollision(Rectangle predicted, boolean horizontal, float proposedTiles, float deltaMoveTiles) {
        int startX = Math.max(0, Math.min((int) (predicted.x / map.getTileSize()), map.width - 1));
        int endX = Math.max(0, Math.min((int) ((predicted.x + predicted.width - EPS) / map.getTileSize()), map.width - 1));
        int startY = Math.max(0, Math.min((int) (predicted.y / map.getTileSize()), map.height - 1));
        int endY = Math.max(0, Math.min((int) ((predicted.y + predicted.height - EPS) / map.getTileSize()), map.height - 1));

        boolean movingPositive = deltaMoveTiles > 0f;

        if (horizontal) {
            if (movingPositive) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[endX][ty].walkable) {
                        return (endX * map.getTileSize() - predicted.width) / map.getTileSize();
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int ty = startY; ty <= endY; ty++) {
                    if (!map.tiles[startX][ty].walkable) {
                        return ((startX + 1f) * map.getTileSize()) / map.getTileSize();
                    }
                }
            }
        } else {
            if (movingPositive) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][endY].walkable) {
                        return (endY * map.getTileSize() - predicted.height) / map.getTileSize();
                    }
                }
            } else if (deltaMoveTiles < 0f) {
                for (int tx = startX; tx <= endX; tx++) {
                    if (!map.tiles[tx][startY].walkable) {
                        return ((startY + 1f) * map.getTileSize()) / map.getTileSize();
                    }
                }
            }
        }
        return proposedTiles;
    }

    private float resolveEntityCollision(EntityPart movingPart, Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles) {
        Set<EntityPart> nearbyParts = new HashSet<>();
        List<Event> delayedEvents = new ArrayList<>();

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
            if (otherPart.getOwner() == movingPart.getOwner()) continue;

            Rectangle otherCollider = otherPart.getCollider();
            if (otherCollider == null) continue;

            Rectangle otherRect = new Rectangle(otherCollider);
            otherRect.setPosition(otherPart.getWorldX(), otherPart.getWorldY());

            if (predicted.overlaps(otherRect)) {
                if (env != null) {
                    delayedEvents.add(new PartCollisionEvent(movingPart, otherPart, env));
                }

                float stop = computeEntityCollisionStop(predicted, otherRect, horizontal);
                if (proposedTiles > startTiles) adjusted = Math.min(adjusted, stop);
                else if (proposedTiles < startTiles) adjusted = Math.max(adjusted, stop);
            }
        }

        for (Event e : delayedEvents) {
            env.getEventBus().post(e);
        }

        return adjusted;
    }

    private float computeEntityCollisionStop(Rectangle self, Rectangle other, boolean horizontal) {
        if (horizontal) {
            return (self.x < other.x ? other.x - self.width : other.x + other.width) / map.getTileSize();
        } else {
            return (self.y < other.y ? other.y - self.height : other.y + other.height) / map.getTileSize();
        }
    }

    public void updateEntityPartsInTiles(Entity entity) {
        Set<TileCoordinate> oldTiles = entity.getOccupiedTiles();

        // The entity has no environment, we need to remove it from the tiles.
        if (entity.getEnvironment() == null) {
            for (TileCoordinate old : oldTiles) {
                if (old.x >= 0 && old.x < map.width && old.y >= 0 && old.y < map.height) {
                    map.tiles[old.x][old.y].removePartsOwnedBy(entity);
                }
            }
            oldTiles.clear();
            return;
        }

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
                    map.tiles[tx][ty].addPart(part);
                }
            }
        });

        for (TileCoordinate old : oldTiles) {
            if (!newTiles.contains(old) && old.x >= 0 && old.x < map.width && old.y >= 0 && old.y < map.height) {
                map.tiles[old.x][old.y].removePartsOwnedBy(entity);
            }
        }

        oldTiles.clear();
        oldTiles.addAll(newTiles);
    }
}
