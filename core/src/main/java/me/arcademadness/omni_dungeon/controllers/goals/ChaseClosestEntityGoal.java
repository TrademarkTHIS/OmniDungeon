package me.arcademadness.omni_dungeon.controllers.goals;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.AStar;
import me.arcademadness.omni_dungeon.environment.world.Tile;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.List;
import java.util.Optional;

public class ChaseClosestEntityGoal<T extends Entity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    private Entity cachedTarget;

    public ChaseClosestEntityGoal(int priority, int scanRadius) {
        this.priority = priority;
        this.scanRadius = scanRadius;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        EnvironmentView env = entity.getEnvironment();
        if (env == null) return Optional.empty();

        TileMap map = env.getMap();

        if (!isValidTarget(cachedTarget)) {
            cachedTarget = findClosestEntity(map, entity, scanRadius);
        }

        if (cachedTarget == null) return Optional.empty();

        Vector2 targetCenter = cachedTarget.getRootPart().getColliderCenter();
        if (targetCenter == null) return Optional.empty();

        Location loc = entity.getLocation();
        List<Vector2> path = AStar.findPath(
            map,
            loc.getX(), loc.getY(),
            targetCenter.x, targetCenter.y
        ,8);

        if (path.isEmpty()) return Optional.empty();

        Vector2 next = path.get(0);
        Vector2 dir = new Vector2(next.x - loc.getX(), next.y - loc.getY()).nor();

        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(dir));
        return Optional.of(intent);
    }

    /**
     * Finds the closest valid target entity within the given radius.
     */
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

                    Vector2 pos = e.getLocation();
                    float distSq = loc.dst2(pos);
                    if (distSq < bestDistSq) {
                        bestDistSq = distSq;
                        closest = e;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Checks if the entity is valid (non-null, has health > 0).
     */
    private boolean isValidTarget(Entity e) {
        return e != null
            && e.getHealth() != null
            && e.getHealth().getCurrentHealth() > 0;
    }
}
