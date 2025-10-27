package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;
import java.awt.Point;
import java.util.*;

/**
 * A* pathfinding that supports float coordinates for precise start/goal positions.
 * Uses Manhattan + diagonal movement.
 */
public class AStar {

    private static final int[][] DIRECTIONS = {
        { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
        { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
    };

    /**
     * Finds a path between two points in tile coordinates (floats allowed).
     * The returned path is a list of Vector2 tile positions (can include .5 centers).
     */
    public static List<Vector2> findPath(TileMap map, float startX, float startY, float goalX, float goalY) {
        int startTileX = (int) Math.floor(startX);
        int startTileY = (int) Math.floor(startY);
        int goalTileX  = (int) Math.floor(goalX);
        int goalTileY  = (int) Math.floor(goalY);

        if (!inBounds(map, startTileX, startTileY) || !inBounds(map, goalTileX, goalTileY))
            return Collections.emptyList();

        Tile start = map.tiles[startTileX][startTileY];
        Tile goal = map.tiles[goalTileX][goalTileY];
        if (!start.walkable || !goal.walkable)
            return Collections.emptyList();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Map<Point, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startTileX, startTileY, null, 0, heuristic(startTileX, startTileY, goalTileX, goalTileY));
        open.add(startNode);
        allNodes.put(new Point(startTileX, startTileY), startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.x == goalTileX && current.y == goalTileY) {
                return reconstructPath(current, goalX, goalY);
            }

            current.closed = true;

            for (int[] dir : DIRECTIONS) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (!inBounds(map, nx, ny)) continue;
                Tile tile = map.tiles[nx][ny];
                if (!tile.walkable) continue;

                Point key = new Point(nx, ny);
                double gCost = current.gCost + ((dir[0] != 0 && dir[1] != 0) ? Math.sqrt(2) : 1);

                Node neighbor = allNodes.get(key);
                if (neighbor == null) {
                    neighbor = new Node(nx, ny, current, gCost, heuristic(nx, ny, goalTileX, goalTileY));
                    allNodes.put(key, neighbor);
                    open.add(neighbor);
                } else if (gCost < neighbor.gCost) {
                    neighbor.parent = current;
                    neighbor.gCost = gCost;
                    neighbor.fCost = gCost + neighbor.hCost;
                    if (!neighbor.closed) {
                        open.remove(neighbor);
                        open.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private static List<Vector2> reconstructPath(Node node, float goalX, float goalY) {
        LinkedList<Vector2> path = new LinkedList<>();
        while (node.parent != null) {
            path.addFirst(new Vector2(node.x + 0.5f, node.y + 0.5f));
            node = node.parent;
        }
        path.add(new Vector2(goalX, goalY));
        return path;
    }

    private static boolean inBounds(TileMap map, int x, int y) {
        return x >= 0 && y >= 0 && x < map.width && y < map.height;
    }

    private static double heuristic(int x, int y, int goalX, int goalY) {
        int dx = Math.abs(x - goalX);
        int dy = Math.abs(y - goalY);
        double F = Math.sqrt(2) - 1;
        return (dx < dy) ? F * dx + dy : F * dy + dx;
    }

    private static class Node {
        int x, y;
        Node parent;
        double gCost; // cost so far
        double hCost; // heuristic cost to goal
        double fCost; // total = g + h
        boolean closed = false;

        Node(int x, int y, Node parent, double gCost, double hCost) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }
}
