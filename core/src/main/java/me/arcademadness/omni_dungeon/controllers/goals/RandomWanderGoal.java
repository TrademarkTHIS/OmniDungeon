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
    public Optional<ControlIntent> computeIntent(T entity) {
        EnvironmentView env = entity.getEnvironment();
        if (env == null) return Optional.empty();

        TileMap map = env.getMap();
        Location loc = entity.getLocation();

        // Pick a new target if we don't have one or we've reached the current target
        if (targetTile == null || path == null || path.isEmpty()) {
            targetTile = pickRandomWalkableTile(map);
            if (targetTile == null) return Optional.empty(); // No walkable tile found

            path = AStar.findPath(
                map,
                loc.getX(), loc.getY(),
                targetTile.x, targetTile.y
            );

            // If pathfinding failed, try another tile next frame
            if (path.isEmpty()) {
                targetTile = null;
                return Optional.empty();
            }
        }

        // Move along the first step of the path
        Vector2 next = path.remove(0);
        Vector2 dir = new Vector2(next.x - loc.getX(), next.y - loc.getY()).nor();

        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(dir));
        return Optional.of(intent);
    }

    private Vector2 pickRandomWalkableTile(TileMap map) {
        int attempts = 20; // Try up to 20 times to find a walkable tile
        for (int i = 0; i < attempts; i++) {
            int x = random.nextInt(map.width);
            int y = random.nextInt(map.height);
            Tile tile = map.tiles[x][y];

            if (tile.isWalkable() && tile.parts.isEmpty()) { // or any other check for occupancy
                return new Vector2(x, y);
            }
        }
        return null;
    }
}
