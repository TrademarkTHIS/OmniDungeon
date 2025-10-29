package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.components.MobGroup;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.Optional;

public class GroupFindTargetGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    public GroupFindTargetGoal(int priority, int scanRadius) {
        this.priority = priority;
        this.scanRadius = scanRadius;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        MobGroup group = entity.getGroup();

        // Only queens can trigger this goal
        if (group == null || group.getQueen() != entity) return false;

        // Only if no valid target exists
        Entity target = group.getTarget();
        return target == null
            || target.getHealth() == null
            || target.getHealth().getCurrentHealth() <= 0;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T queen) {
        MobGroup group = queen.getGroup();
        if (group == null) return Optional.empty();

        TileMap map = queen.getEnvironment().getMap();
        if (map == null) return Optional.empty();

        Entity target = findClosestEnemy(queen, map, scanRadius);

        // If found, assign to the whole group
        if (target != null) {
            group.setTarget(target);
        }

        // This goal doesn’t directly control movement — it’s informational
        return Optional.empty();
    }

    private Entity findClosestEnemy(T self, TileMap map, int radius) {
        Location loc = self.getLocation();
        Entity closest = null;
        float bestDistSq = Float.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = loc.getTileX() + dx;
                int y = loc.getTileY() + dy;
                if (x < 0 || y < 0 || x >= map.width || y >= map.height) continue;

                var tile = map.tiles[x][y];
                for (var part : tile.parts) {
                    Entity e = part.getOwner();
                    if (e == self) continue;
                    if (self.getClass().isInstance(e)) continue; // skip same-type mobs
                    if (e.getHealth() == null || e.getHealth().getCurrentHealth() <= 0) continue;

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
}
