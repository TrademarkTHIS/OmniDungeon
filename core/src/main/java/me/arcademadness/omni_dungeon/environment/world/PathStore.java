package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class PathStore {

    private final List<List<Vector2>> paths = new ArrayList<>();
    private final Map<Vector2, List<PointIndex>> pointIndex = new HashMap<>();

    private static class PointIndex {
        int pathId;
        int index;
        PointIndex(int pathId, int index) {
            this.pathId = pathId;
            this.index = index;
        }
    }

    /**
     * Adds a path to the store.
     */
    public void addPath(List<Vector2> path) {
        int pathId = paths.size();
        paths.add(path);
        for (int i = 0; i < path.size(); i++) {
            Vector2 point = path.get(i);
            pointIndex.computeIfAbsent(point, k -> new ArrayList<>())
                      .add(new PointIndex(pathId, i));
        }
    }

    /**
     * Retrieves a subpath starting at 'start' and ending at 'end', if it exists.
     */
    public List<Vector2> getSubpath(Vector2 start, Vector2 end) {
        List<PointIndex> candidates = pointIndex.getOrDefault(start, Collections.emptyList());

        for (PointIndex pi : candidates) {
            List<Vector2> path = paths.get(pi.pathId);
            int startIdx = pi.index;

            for (int i = startIdx; i < path.size(); i++) {
                if (path.get(i).epsilonEquals(end, 0.0001f)) {
                    return path.subList(startIdx, i + 1);
                }
            }
        }

        return Collections.emptyList(); // subpath not found
    }

    /**
     * Retrieve all subpaths starting at a given point
     */
    public List<List<Vector2>> getSubpathsFrom(Vector2 start) {
        List<List<Vector2>> results = new ArrayList<>();
        List<PointIndex> candidates = pointIndex.getOrDefault(start, Collections.emptyList());
        for (PointIndex pi : candidates) {
            List<Vector2> path = paths.get(pi.pathId);
            results.add(path.subList(pi.index, path.size()));
        }
        return results;
    }
}
