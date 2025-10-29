package me.arcademadness.omni_dungeon.controllers.goals;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.controllers.ControlIntent;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.AStar;
import me.arcademadness.omni_dungeon.environment.world.Tile;
import me.arcademadness.omni_dungeon.environment.world.TileMap;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomWanderGoal<T extends Entity> implements Goal<T> {

    private static final Random random = new Random();

    private final int priority;
    private Vector2 targetTile = null;
    private List<Vector2> path = null;

    public RandomWanderGoal(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean shouldActivate(T entity) {
        return true;
    }

    @Override
    public Optional<ControlIntent> computeIntent(T entity) {
        EnvironmentView env = entity.getEnvironment();
        if (env == null) return Optional.empty();

        TileMap map = env.getMap();
        Location loc = entity.getLocation();

        if (targetTile == null || path == null || path.isEmpty()) {
            targetTile = pickRandomWalkableTile(map);
            if (targetTile == null) return Optional.empty();

            path = AStar.findPath(
                map,
                loc.getX(), loc.getY(),
                targetTile.x, targetTile.y
            ,8);

            if (path.isEmpty()) {
                targetTile = null;
                return Optional.empty();
            }
        }

        Vector2 next = path.remove(0);
        Vector2 dir = new Vector2(next.x - loc.getX(), next.y - loc.getY()).nor();

        Vector2 separation = new Vector2();
        int beeTileX = loc.getTileX();
        int beeTileY = loc.getTileY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int tx = beeTileX + dx;
                int ty = beeTileY + dy;
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

    private Vector2 pickRandomWalkableTile(TileMap map) {
        int attempts = 20;
        for (int i = 0; i < attempts; i++) {
            int x = random.nextInt(map.width);
            int y = random.nextInt(map.height);
            Tile tile = map.tiles[x][y];

            if (tile.isWalkable() && tile.parts.isEmpty()) {
                return new Vector2(x, y);
            }
        }
        return null;
    }
}
