package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class PathStore {

    /**
     * nextHop[current][destination] = next step
     */
    private final Map<Vector2, Map<Vector2, Vector2>> nextHop = new HashMap<>();

    private static final float EPS = 0.0001f;

    /**
     * Adds a computed path to the cache.
     *
     * Example path:
     * A -> C -> D -> B
     *
     * Stored as:
     * (A,B) -> C
     * (C,B) -> D
     * (D,B) -> B
     */
    public void addPath(List<Vector2> path) {

        if (path == null || path.size() < 2)
            return;

        Vector2 destination = path.get(path.size() - 1);

        for (int i = 0; i < path.size() - 1; i++) {

            Vector2 current = path.get(i);
            Vector2 next = path.get(i + 1);

            nextHop
                .computeIfAbsent(current, k -> new HashMap<>())
                .put(destination, next);
        }
    }

    /**
     * Returns a cached path if one exists.
     */
    public List<Vector2> getPath(Vector2 start, Vector2 end) {

        List<Vector2> result = new ArrayList<>();

        Vector2 current = start;
        result.add(current);

        Set<Vector2> visited = new HashSet<>();

        while (!epsilonEquals(current, end)) {

            if (!visited.add(current)) {
                // cycle detected
                return Collections.emptyList();
            }

            Map<Vector2, Vector2> table = nextHop.get(current);

            if (table == null)
                return Collections.emptyList();

            Vector2 next = findNextHop(table, end);

            if (next == null)
                return Collections.emptyList();

            result.add(next);
            current = next;
        }

        return result;
    }

    /**
     * Returns true if we have a cached route.
     */
    public boolean hasPath(Vector2 start, Vector2 end) {

        Map<Vector2, Vector2> table = nextHop.get(start);

        if (table == null)
            return false;

        return findNextHop(table, end) != null;
    }

    /**
     * Returns the next step toward a destination if cached.
     */
    public Vector2 getNextHop(Vector2 current, Vector2 destination) {

        Map<Vector2, Vector2> table = nextHop.get(current);

        if (table == null)
            return null;

        return findNextHop(table, destination);
    }

    /**
     * Used during pathfinding to see if we can shortcut a search.
     */
    public boolean canShortcut(Vector2 current, Vector2 destination) {
        return hasPath(current, destination);
    }

    /**
     * Returns cached continuation path from current node.
     */
    public List<Vector2> getContinuation(Vector2 current, Vector2 destination) {
        return getPath(current, destination);
    }

    /**
     * Finds next hop using epsilon comparison.
     */
    private Vector2 findNextHop(Map<Vector2, Vector2> table, Vector2 destination) {

        for (Map.Entry<Vector2, Vector2> entry : table.entrySet()) {

            if (epsilonEquals(entry.getKey(), destination))
                return entry.getValue();
        }

        return null;
    }

    private boolean epsilonEquals(Vector2 a, Vector2 b) {
        return a.epsilonEquals(b, EPS);
    }

}
