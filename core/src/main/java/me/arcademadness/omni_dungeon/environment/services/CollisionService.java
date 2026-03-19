package me.arcademadness.omni_dungeon.environment.services;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.environment.world.TileCoordinate;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.Floor;
import me.arcademadness.omni_dungeon.environment.world.Tile;
import me.arcademadness.omni_dungeon.events.Event;
import me.arcademadness.omni_dungeon.events.collision.PartCollisionEvent;
import me.arcademadness.omni_dungeon.events.collision.TileCollisionEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles entity–tile and entity–entity collision resolution.
 * <p>
 * This version is fully hierarchy-aware: every {@link EntityPart}, including nested
 * child parts, is considered in collision and tile occupancy.
 * </p>
 */
public class CollisionService {

    private final Floor map;
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
        final float[] adjustedResult = {proposed};

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
            adjusted = resolveTileCollision(predicted, horizontal, adjusted, deltaMoveTiles, part);
            adjusted = resolveEntityCollision(part, predicted, horizontal, start, adjusted);

            adjustedResult[0] = adjusted;
        });

        return adjustedResult[0];
    }

    private float resolveTileCollision(Rectangle predicted, boolean horizontal, float proposedTiles, float deltaMoveTiles, EntityPart movingPart) {
        Floor.TileBounds bounds = map.getTileBoundsForRect(predicted.x, predicted.y, predicted.width, predicted.height, EPS);
        boolean movingPositive = deltaMoveTiles > 0f;

        int primary = horizontal
            ? (movingPositive ? bounds.maxXInclusive : bounds.minX)
            : (movingPositive ? bounds.maxYInclusive : bounds.minY);

        int secStart = horizontal ? bounds.minY : bounds.minX;
        int secEnd = horizontal ? bounds.maxYInclusive : bounds.maxXInclusive;

        for (int sec = secStart; sec <= secEnd; sec++) {
            Tile tile = horizontal
                ? map.getTile(primary, sec)
                : map.getTile(sec, primary);

            if (tile != null && !tile.isWalkable()) {
                if (env != null && movingPart != null) {
                    env.getEventBus().post(new TileCollisionEvent(movingPart, tile, env));
                }

                if (horizontal) {
                    return movingPositive
                        ? (primary * map.getTileSize() - predicted.width) / map.getTileSize()
                        : ((primary + 1f) * map.getTileSize()) / map.getTileSize();
                } else {
                    return movingPositive
                        ? (primary * map.getTileSize() - predicted.height) / map.getTileSize()
                        : ((primary + 1f) * map.getTileSize()) / map.getTileSize();
                }
            }
        }

        return proposedTiles;
    }

    private float resolveEntityCollision(EntityPart movingPart, Rectangle predicted, boolean horizontal, float startTiles, float proposedTiles) {
        Set<EntityPart> nearbyParts = new HashSet<>();
        List<Event> delayedEvents = new ArrayList<>();

        Floor.TileBounds bounds = map.getTileBoundsForRect(predicted.x, predicted.y, predicted.width, predicted.height, EPS);

        for (int tx = bounds.minX; tx <= bounds.maxXInclusive; tx++) {
            for (int ty = bounds.minY; ty <= bounds.maxYInclusive; ty++) {
                Tile tile = map.getTile(tx, ty);
                if (tile != null) {
                    nearbyParts.addAll(tile.parts);
                }
            }
        }

        float adjusted = proposedTiles;

        for (EntityPart otherPart : nearbyParts) {
            if (otherPart.getOwner() == movingPart.getOwner()) continue;

            /*
              This needs to be replaced for more robust checking
              Maybe completely different types of entities don't need to collide
             */
            if (otherPart.getOwner().getClass() == movingPart.getOwner().getClass()) continue;

            Rectangle otherCollider = otherPart.getCollider();
            if (otherCollider == null) continue;

            Rectangle otherRect = new Rectangle(otherCollider);
            otherRect.setPosition(otherPart.getWorldX(), otherPart.getWorldY());

            if (predicted.overlaps(otherRect)) {
                if (env != null) {
                    delayedEvents.add(new PartCollisionEvent(movingPart, otherPart, env));
                }

                float stop = computeEntityCollisionStop(predicted, otherRect, horizontal);
                if (proposedTiles > startTiles) {
                    adjusted = Math.min(adjusted, stop);
                } else if (proposedTiles < startTiles) {
                    adjusted = Math.max(adjusted, stop);
                }
            }
        }

        for (Event event : delayedEvents) {
            env.getEventBus().post(event);
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
                Tile tile = map.getTile(old.x, old.y);
                if (tile != null) {
                    tile.removePartsOwnedBy(entity);
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

            map.forEachTileInRect(worldX, worldY, collider.width, collider.height, EPS, (tx, ty, tile) -> {
                TileCoordinate tc = new TileCoordinate(tx, ty);
                newTiles.add(tc);
                tile.addPart(part);
            });
        });

        for (TileCoordinate old : oldTiles) {
            if (!newTiles.contains(old)) {
                Tile tile = map.getTile(old.x, old.y);
                if (tile != null) {
                    tile.removePartsOwnedBy(entity);
                }
            }
        }

        oldTiles.clear();
        oldTiles.addAll(newTiles);
    }

    public void checkImmediateCollisions(Entity entity) {
        entity.getRootPart().forEachPart(part -> {
            Rectangle collider = part.getCollider();
            if (collider == null) return;

            Rectangle selfRect = new Rectangle(collider);
            selfRect.setPosition(part.getWorldX(), part.getWorldY());

            Set<EntityPart> nearby = new HashSet<>();

            map.forEachTileInRect(part.getWorldX(), part.getWorldY(), collider.width, collider.height, EPS, (tx, ty, tile) -> {
                nearby.addAll(tile.parts);
            });

            for (EntityPart other : nearby) {
                if (other.getOwner() == part.getOwner()) continue;

                Rectangle otherCollider = other.getCollider();
                if (otherCollider == null) continue;

                Rectangle otherRect = new Rectangle(otherCollider);
                otherRect.setPosition(other.getWorldX(), other.getWorldY());

                if (selfRect.overlaps(otherRect)) {
                    env.getEventBus().post(new PartCollisionEvent(part, other, env));
                }
            }
        });
    }
}
