package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.components.MobGroup;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.Optional;

public class FindTargetGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    public FindTargetGoal(int priority, int scanRadius) {
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
        return group != null && (group.getTarget() == null || group.getTarget().getHealth().getCurrentHealth() <= 0);
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        MobGroup group = entity.getGroup();
        TileMap map = entity.getEnvironment().getMap();

        Entity target = findClosestEnemy(entity, map, scanRadius);
        group.setTarget(target);

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
                    if (self.getClass().isInstance(e)) continue; // skip same type

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
