package me.arcademadness.omni_dungeon.controllers.goals;

import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.components.MobGroup;
import me.arcademadness.omni_dungeon.components.Location;

import java.util.Optional;

public class FindGroupGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;
    private final int scanRadius;

    public FindGroupGoal(int priority, int scanRadius) {
        this.priority = priority;
        this.scanRadius = scanRadius;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        // Activate if the entity has no group yet
        return entity.getGroup() == null;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        TileMap map = entity.getEnvironment().getMap();
        MobGroup foundGroup = findNearbyGroup(entity, map);

        if (foundGroup != null) {
            foundGroup.addMember(entity);
            entity.setGroup(foundGroup);
        }

        return Optional.empty(); // no movement or action needed for just finding a group
    }

    private MobGroup findNearbyGroup(T self, TileMap map) {
        Location loc = self.getLocation();

        for (int dx = -scanRadius; dx <= scanRadius; dx++) {
            for (int dy = -scanRadius; dy <= scanRadius; dy++) {
                int x = loc.getTileX() + dx;
                int y = loc.getTileY() + dy;
                if (x < 0 || y < 0 || x >= map.width || y >= map.height) continue;

                var tile = map.tiles[x][y];
                for (var part : tile.parts) {
                    var other = part.getOwner();
                    if (other == self) continue;
                    if (!self.getClass().isInstance(other)) continue;

                    MobEntity mob = (MobEntity) other;
                    if (mob.getGroup() != null) return mob.getGroup();
                }
            }
        }

        return null;
    }
}
