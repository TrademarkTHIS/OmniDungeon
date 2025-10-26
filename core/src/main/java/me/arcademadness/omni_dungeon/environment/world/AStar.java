package me.arcademadness.omni_dungeon.environment.world;

import java.awt.Point;
import java.util.*;

/**
 * Simple A* pathfinding over a TileMap.
 * Uses Manhattan distance and supports 4-directional movement.
 */
public class AStar {

    private static final int[][] DIRECTIONS = {
        { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
        { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
    };


    public static List<Point> findPath(TileMap map, int startX, int startY, int goalX, int goalY) {
        // Basic bounds check
        if (!inBounds(map, startX, startY) || !inBounds(map, goalX, goalY))
            return Collections.emptyList();

        Tile start = map.tiles[startX][startY];
        Tile goal = map.tiles[goalX][goalY];
        if (!start.walkable || !goal.walkable)
            return Collections.emptyList();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Map<Point, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY, null, 0, heuristic(startX, startY, goalX, goalY));
        open.add(startNode);
        allNodes.put(new Point(startX, startY), startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
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
                    neighbor = new Node(nx, ny, current, gCost, heuristic(nx, ny, goalX, goalY));
                    allNodes.put(key, neighbor);
                    open.add(neighbor);
                } else if (gCost < neighbor.gCost) {
                    neighbor.parent = current;
                    neighbor.gCost = gCost;
                    neighbor.fCost = gCost + neighbor.hCost;
                    if (!neighbor.closed) {
                        // Reinsert to update priority (simpler than decrease-key)
                        open.remove(neighbor);
                        open.add(neighbor);
                    }
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    private static List<Point> reconstructPath(Node node) {
        LinkedList<Point> path = new LinkedList<>();
        while (node.parent != null) {
            path.addFirst(new Point(node.x, node.y));
            node = node.parent;
        }
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
