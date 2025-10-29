package me.arcademadness.omni_dungeon.controllers.goals;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.components.MobGroup;
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

public class GroupChaseGoal<T extends MobEntity> implements Goal<T> {

    private final int priority;

    public GroupChaseGoal(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        MobGroup group = entity.getGroup();
        return group != null
            && group.getTarget() != null
            && group.getTarget().getHealth() != null
            && group.getTarget().getHealth().getCurrentHealth() > 0;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T self) {
        MobGroup group = self.getGroup();
        if (group == null) return Optional.empty();

        Entity target = group.getTarget();
        if (target == null) return Optional.empty();

        EnvironmentView env = self.getEnvironment();
        if (env == null) return Optional.empty();
        TileMap map = env.getMap();

        Location loc = self.getLocation();
        Vector2 targetCenter = target.getRootPart().getColliderCenter();
        if (targetCenter == null) return Optional.empty();

        // Compute path using A*
        List<Vector2> path = AStar.findPath(
            map,
            loc.getX(), loc.getY(),
            targetCenter.x, targetCenter.y,
            4
        );

        if (path.isEmpty()) return Optional.empty();

        Vector2 next = path.get(0);
        Vector2 dir = new Vector2(next.x - loc.getX(), next.y - loc.getY()).nor();

        // Separation logic: avoid crowding
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
                    if (other == self) continue;
                    if (!self.getClass().isInstance(other)) continue; // same type mobs only

                    Vector2 offset = loc.cpy().sub(other.getLocation());
                    float distSq = offset.len2();
                    float minDist = 0.5f;
                    if (distSq < minDist * minDist && distSq > 0) {
                        separation.add(offset.nor().scl(minDist - (float)Math.sqrt(distSq)));
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
}
