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

    /**
     * Really lame version of the other one. Do not use this.
     * @param map
     * @param startX
     * @param startY
     * @param goalX
     * @param goalY
     * @param chunkRadiusTiles
     * @return
     */
    public static List<Vector2> findPath(Floor map, float startX, float startY, float goalX, float goalY, int chunkRadiusTiles ) {
     return findPath(map, new Vector2(startX, startY), new Vector2(goalX, goalY), chunkRadiusTiles);
    }

    /**
     * Find a path from vector A to B (Uses A*)
     * Replace with a flow field pathfinding system later pls
     * @param map The current floor we're pathing through
     * @param startVec The starting vector
     * @param goalVec The goal vector
     * @param chunkRadiusTiles The radius of tiles to look through for a path
     * @return A list (our path) to the goal vector
     */
    public static List<Vector2> findPath(Floor map, Vector2 startVec, Vector2 goalVec, int chunkRadiusTiles) {
        List<Vector2> cached = PATH_CACHE.getPath(startVec, goalVec);
        if (!cached.isEmpty()) {
            return cached;
        }

        int startTileX = map.worldToTileX(startVec.x);
        int startTileY = map.worldToTileY(startVec.y);
        int goalTileX = map.worldToTileX(goalVec.x);
        int goalTileY = map.worldToTileY(goalVec.y);

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
                List<Vector2> path = reconstructPath(map, current, goalVec.x, goalVec.y);
                if (!path.isEmpty()) {
                    PATH_CACHE.addPath(path);
                }
                return path;
            }

            current.closed = true;

            final Node currentNode = current;
            map.forEachNeighbor8(current.x, current.y, (nx, ny, tile) -> {
                boolean b = nx < minX || nx > maxX || ny < minY || ny > maxY;
                if (currentNode.closed && b) {
                    return;
                }

                if (b) {
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
