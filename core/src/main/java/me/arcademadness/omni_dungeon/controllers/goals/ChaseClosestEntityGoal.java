package me.arcademadness.omni_dungeon.controllers.goals;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.AStar;
import me.arcademadness.omni_dungeon.environment.world.Tile;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.List;
import java.util.Optional;

public class ChaseClosestEntityGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    public ChaseClosestEntityGoal(int priority, int scanRadius) {
        this.priority = priority;
        this.scanRadius = scanRadius;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        return entity.getGroup() != null;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        EnvironmentView env = entity.getEnvironment();
        if (env == null) return Optional.empty();

        TileMap map = env.getMap();

        // Use entity's internal target instead of cachedTarget
        Entity target = entity.getTarget();
        if (!isValidTarget(target)) {
            target = findClosestEntity(map, entity, scanRadius);
            entity.setTarget(target); // update mob's internal target
        }

        if (target == null) return Optional.empty();

        Vector2 targetCenter = target.getRootPart().getColliderCenter();
        if (targetCenter == null) return Optional.empty();

        Location loc = entity.getLocation();
        List<Vector2> path = AStar.findPath(
            map,
            loc.getX(), loc.getY(),
            targetCenter.x, targetCenter.y,
            4
        );

        if (path.isEmpty()) return Optional.empty();

        Vector2 next = path.get(0);
        Vector2 dir = new Vector2(next.x - loc.getX(), next.y - loc.getY()).nor();

        // Separation logic to avoid crowding
        Vector2 separation = new Vector2();
        int tileX = loc.getTileX();
        int tileY = loc.getTileY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int tx = tileX + dx;
                int ty = tileY + dy;
                if (tx < 0 || ty < 0 || tx >= map.width || ty >= map.height) continue;

                Tile tile = map.tiles[tx][ty];
                for (var part : tile.parts) {
                    Entity other = part.getOwner();
                    if (other == entity) continue;
                    if (other.getClass().isInstance(entity)) {
                        Vector2 offset = loc.cpy().sub(other.getLocation());
                        float distSq = offset.len2();
                        float minDist = 0.5f;
                        if (distSq < minDist * minDist && distSq > 0) {
                            separation.add(offset.nor().scl((minDist - (float)Math.sqrt(distSq))));
                        }
                    }
                }
            }
        }

        if (separation.len2() > 0) {
            separation.nor().scl(0.5f);
            dir.add(separation).nor();
        }

        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(dir));
        return Optional.of(intent);
    }

    private Entity findClosestEntity(TileMap map, T self, int radius) {
        Location loc = self.getLocation();
        Entity closest = null;
        float bestDistSq = Float.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = loc.getTileX() + dx;
                int y = loc.getTileY() + dy;
                if (x < 0 || y < 0 || x >= map.width || y >= map.height) continue;

                Tile tile = map.tiles[x][y];
                for (var part : tile.parts) {
                    Entity e = part.getOwner();
                    if (e == self) continue;
                    if (self.getClass().isInstance(e)) continue; // Skip same type

                    if (!isValidTarget(e)) continue;

                    float distSq = loc.dst2(e.getLocation());
                    if (distSq < bestDistSq) {
                        bestDistSq = distSq;
                        closest = e;
                    }
                }
            }
        }
        return closest;
    }

    private boolean isValidTarget(Entity e) {
        return e != null
            && e.getHealth() != null
            && e.getHealth().getCurrentHealth() > 0;
    }
}
