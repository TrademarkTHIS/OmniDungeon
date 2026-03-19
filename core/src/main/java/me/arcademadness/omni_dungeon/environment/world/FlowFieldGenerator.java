package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public class FlowFieldGenerator {

    private static final int[][] DIRECTIONS = {
            {1,0},{-1,0},{0,1},{0,-1},
            {1,1},{1,-1},{-1,1},{-1,-1}
    };

    public static FlowField generate(Floor map, int goalX, int goalY) {

        FlowField field = new FlowField(map.width, map.height);

        Queue<Point> open = new ArrayDeque<>();

        field.setCost(goalX, goalY, 0);
        open.add(new Point(goalX, goalY));

        while (!open.isEmpty()) {

            Point p = open.poll();

            float currentCost = field.getCost(p.x, p.y);

            for (int[] dir : DIRECTIONS) {

                int nx = p.x + dir[0];
                int ny = p.y + dir[1];

                if (nx < 0 || ny < 0 || nx >= map.width || ny >= map.height)
                    continue;

                Tile tile = map.tiles[nx][ny];

                if (!tile.walkable)
                    continue;

                float newCost = currentCost + 1;

                if (newCost < field.getCost(nx, ny)) {

                    field.setCost(nx, ny, newCost);

                    open.add(new Point(nx, ny));

                }
            }
        }

        // ------------------------------------------------
        // BUILD FLOW FIELD (vector pointing downhill)
        // ------------------------------------------------

        for (int x = 0; x < map.width; x++) {

            for (int y = 0; y < map.height; y++) {

                if (!map.tiles[x][y].walkable)
                    continue;

                float bestCost = field.getCost(x, y);

                Vector2 bestDir = new Vector2();

                for (int[] dir : DIRECTIONS) {

                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (nx < 0 || ny < 0 || nx >= map.width || ny >= map.height)
                        continue;

                    float neighborCost = field.getCost(nx, ny);

                    if (neighborCost < bestCost) {

                        bestCost = neighborCost;

                        bestDir.set(dir[0], dir[1]);

                    }
                }

                if (!bestDir.isZero()) {
                    bestDir.nor();
                }

                field.setDirection(x, y, bestDir);
            }
        }

        return field;
    }

    public static void updatePartial(FlowField field, Floor map, int oldX, int oldY, int newX, int newY) {

        Queue<Point> open = new ArrayDeque<>();
        field.setCost(newX, newY, 0);
        open.add(new Point(newX, newY));

        // BFS update for affected area
        while (!open.isEmpty()) {
            Point p = open.poll();
            float currentCost = field.getCost(p.x, p.y);

            for (int[] dir : DIRECTIONS) {
                int nx = p.x + dir[0];
                int ny = p.y + dir[1];

                if (nx < 0 || ny < 0 || nx >= map.width || ny >= map.height) continue;
                Tile tile = map.tiles[nx][ny];
                if (!tile.walkable) continue;

                float newCost = currentCost + 1;

                if (newCost < field.getCost(nx, ny)) {
                    field.setCost(nx, ny, newCost);
                    open.add(new Point(nx, ny));
                }
            }
        }

        // Rebuild flow directions in the updated area (2-tile padding)
        int minX = Math.max(0, Math.min(oldX, newX) - 2);
        int maxX = Math.min(map.width - 1, Math.max(oldX, newX) + 2);
        int minY = Math.max(0, Math.min(oldY, newY) - 2);
        int maxY = Math.min(map.height - 1, Math.max(oldY, newY) + 2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (!map.tiles[x][y].walkable) continue;

                float bestCost = field.getCost(x, y);
                Vector2 bestDir = new Vector2();

                for (int[] dir : DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx < 0 || ny < 0 || nx >= map.width || ny >= map.height) continue;

                    float neighborCost = field.getCost(nx, ny);
                    if (neighborCost < bestCost) {
                        bestCost = neighborCost;
                        bestDir.set(dir[0], dir[1]);
                    }
                }

                if (!bestDir.isZero()) bestDir.nor();
                field.setDirection(x, y, bestDir);
            }
        }
    }
}
