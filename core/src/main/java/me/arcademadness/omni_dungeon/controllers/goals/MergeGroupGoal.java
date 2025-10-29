package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.components.MobGroup;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.Optional;

public class MergeGroupGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    public MergeGroupGoal(int priority, int scanRadius) {
        this.priority = priority;
        this.scanRadius = scanRadius;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        // Only activate if the entity has a group
        return entity.getGroup() != null;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        MobGroup group = entity.getGroup();
        if (group == null) return Optional.empty();

        TileMap map = entity.getEnvironment().getMap();
        Location loc = entity.getLocation();

        for (int dx = -scanRadius; dx <= scanRadius; dx++) {
            for (int dy = -scanRadius; dy <= scanRadius; dy++) {
                int x = loc.getTileX() + dx;
                int y = loc.getTileY() + dy;
                if (x < 0 || y < 0 || x >= map.width || y >= map.height) continue;

                var tile = map.tiles[x][y];
                for (var part : tile.parts) {
                    var other = part.getOwner();
                    if (other == entity) continue;
                    if (!entity.getClass().isInstance(other)) continue;

                    MobEntity mob = (MobEntity) other;
                    MobGroup otherGroup = mob.getGroup();
                    if (otherGroup != null && otherGroup != group && group.canMerge(otherGroup)) {
                        group.merge(otherGroup);
                    }
                }
            }
        }

        return Optional.empty();
    }
}
