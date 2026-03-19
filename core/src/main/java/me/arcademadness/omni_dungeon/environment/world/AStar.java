package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BinaryHeap;
import me.arcademadness.omni_dungeon.components.Location;

import java.awt.Point;
import java.util.*;

/**
 * A* pathfinding that supports float coordinates for precise start/goal positions.
 * Uses Manhattan + diagonal movement.
 */
public class AStar {

    private static final PathStore PATH_CACHE = new PathStore();
    private static final double DIAGONAL_COST = Math.sqrt(2.0);

    public static List<Vector2> findPath(Floor map, Location start, Location goal, int chunkRadiusTiles) {
        return findPath(map, start.getX(), start.getY(), goal.getX(), goal.getY(), chunkRadiusTiles);
    }

    public static List<Vector2> findPath(
        Floor map,
        float startX,
        float startY,
        float goalX,
        float goalY,
        int chunkRadiusTiles
    ) {
        Vector2 startVec = new Vector2(startX, startY);
        Vector2 goalVec = new Vector2(goalX, goalY);

        List<Vector2> cached = PATH_CACHE.getPath(startVec, goalVec);
        if (!cached.isEmpty()) {
            return cached;
        }

        int startTileX = map.worldToTileX(startX);
        int startTileY = map.worldToTileY(startY);
        int goalTileX = map.worldToTileX(goalX);
        int goalTileY = map.worldToTileY(goalY);

        if (!map.inBounds(startTileX, startTileY) || !map.inBounds(goalTileX, goalTileY)) {
            return Collections.emptyList();
        }

        if (!map.isWalkable(startTileX, startTileY) || !map.isWalkable(goalTileX, goalTileY)) {
            return Collections.emptyList();
        }

        int halfChunk = chunkRadiusTiles / 2;
        int minX = map.clampTileX(startTileX - halfChunk);
        int maxX = map.clampTileX(startTileX + halfChunk);
        int minY = map.clampTileY(startTileY - halfChunk);
        int maxY = map.clampTileY(startTileY + halfChunk);

        BinaryHeap<Node> open = new BinaryHeap<>();
        Map<Point, Node> allNodes = new HashMap<>();

        Node startNode = new Node(
            startTileX,
            startTileY,
            null,
            0.0,
            heuristic(startTileX, startTileY, goalTileX, goalTileY)
        );

        open.add(startNode);
        allNodes.put(new Point(startTileX, startTileY), startNode);

        final Node[] bestNode = {startNode};

        while (open.size > 0) {
            Node current = open.pop();

            if (current.closed) {
                continue;
            }

            if (current.x == goalTileX && current.y == goalTileY) {
                List<Vector2> path = reconstructPath(map, current, goalX, goalY);
                if (!path.isEmpty()) {
                    PATH_CACHE.addPath(path);
                }
                return path;
            }

            current.closed = true;

            final Node currentNode = current;
            map.forEachNeighbor8(current.x, current.y, (nx, ny, tile) -> {
                if (currentNode.closed && (nx < minX || nx > maxX || ny < minY || ny > maxY)) {
                    return;
                }

                if (nx < minX || nx > maxX || ny < minY || ny > maxY) {
                    return;
                }

                if (!tile.isWalkable()) {
                    return;
                }

                double stepCost = (nx != currentNode.x && ny != currentNode.y) ? DIAGONAL_COST : 1.0;
                double gCost = currentNode.gCost + stepCost;

                Point key = new Point(nx, ny);
                Node neighbor = allNodes.get(key);

                if (neighbor == null) {
                    neighbor = new Node(
                        nx,
                        ny,
                        currentNode,
                        gCost,
                        heuristic(nx, ny, goalTileX, goalTileY)
                    );
                    allNodes.put(key, neighbor);
                    open.add(neighbor);
                } else if (!neighbor.closed && gCost < neighbor.gCost) {
                    neighbor.updateCosts(currentNode, gCost);
                    open.setValue(neighbor, (float) neighbor.fCost);
                }

                if (heuristic(nx, ny, goalTileX, goalTileY) <
                    heuristic(bestNode[0].x, bestNode[0].y, goalTileX, goalTileY)) {
                    bestNode[0] = neighbor;
                }
            });
        }

        if (bestNode[0] != startNode) {
            float fallbackGoalX = map.toPixelX(bestNode[0].x) + map.getTileSize() * 0.5f;
            float fallbackGoalY = map.toPixelY(bestNode[0].y) + map.getTileSize() * 0.5f;

            List<Vector2> path = reconstructPath(map, bestNode[0], fallbackGoalX, fallbackGoalY);
            if (!path.isEmpty()) {
                PATH_CACHE.addPath(path);
            }
            return path;
        }

        return Collections.emptyList();
    }

    private static List<Vector2> reconstructPath(Floor map, Node node, float goalX, float goalY) {
        LinkedList<Vector2> path = new LinkedList<>();

        while (node.parent != null) {
            float centerX = map.toPixelX(node.x) + map.getTileSize() * 0.5f;
            float centerY = map.toPixelY(node.y) + map.getTileSize() * 0.5f;
            path.addFirst(new Vector2(centerX, centerY));
            node = node.parent;
        }

        path.add(new Vector2(goalX, goalY));
        return path;
    }

    private static double heuristic(int x, int y, int goalX, int goalY) {
        int dx = Math.abs(x - goalX);
        int dy = Math.abs(y - goalY);
        double f = Math.sqrt(2.0) - 1.0;
        return (dx < dy) ? (f * dx + dy) : (f * dy + dx);
    }

    private static class Node extends BinaryHeap.Node {
        int x;
        int y;
        Node parent;
        double gCost;
        double hCost;
        double fCost;
        boolean closed;

        Node(int x, int y, Node parent, double gCost, double hCost) {
            super((float) (gCost + hCost));
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.closed = false;
        }

        void updateCosts(Node parent, double newGCost) {
            this.parent = parent;
            this.gCost = newGCost;
            this.fCost = newGCost + hCost;
        }
    }
}
