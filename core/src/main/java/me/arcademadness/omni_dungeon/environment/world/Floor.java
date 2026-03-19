package me.arcademadness.omni_dungeon.environment.world;

import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;

public class Floor {

    private final int tileSize = EnvironmentConfig.get().getTileSize();
    private final int chunkSize = EnvironmentConfig.get().getChunkSize();

    public final Tile[][] tiles;
    public final int width;
    public final int height;
    public final Pathfinder pathfinder = new Pathfinder(this);

    public Floor(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        generate();
    }

    // ----------------------------------------------------------------
    // Helper types
    // ----------------------------------------------------------------

    public record ChunkPos(int x, int y) {}

    public record TileBounds(int minX, int minY, int maxXInclusive, int maxYInclusive) {}

    @FunctionalInterface
    public interface TileVisitor {
        void visit(int x, int y, Tile tile);
    }

    @FunctionalInterface
    public interface ChunkVisitor {
        void visit(int chunkX, int chunkY, int minX, int minY, int maxXExclusive, int maxYExclusive);
    }

    // ----------------------------------------------------------------
    // Generation
    // ----------------------------------------------------------------

    private void generate() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean isBorder = x == 0 || y == 0 || x == width - 1 || y == height - 1;
                tiles[x][y] = new Tile(!isBorder);
            }
        }
    }

    // ----------------------------------------------------------------
    // Basic properties
    // ----------------------------------------------------------------

    public int getTileSize() {
        return tileSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    // ----------------------------------------------------------------
    // Tile access
    // ----------------------------------------------------------------

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Tile getTile(int x, int y) {
        return inBounds(x, y) ? tiles[x][y] : null;
    }

    public void setTile(int x, int y, Tile tile) {
        if (inBounds(x, y)) {
            tiles[x][y] = tile;
        }
    }

    public boolean isWalkable(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null && tile.isWalkable();
    }

    public int toIndex(int x, int y) {
        validateTileBounds(x, y);
        return y * width + x;
    }

    // ----------------------------------------------------------------
    // Tile / world conversion
    // ----------------------------------------------------------------

    public int toPixelX(int tileX) {
        return tileX * tileSize;
    }

    public int toPixelY(int tileY) {
        return tileY * tileSize;
    }

    public int toTileX(int pixelX) {
        return pixelX / tileSize;
    }

    public int toTileY(int pixelY) {
        return pixelY / tileSize;
    }

    public int worldToTileX(float worldX) {
        return (int) (worldX / tileSize);
    }

    public int worldToTileY(float worldY) {
        return (int) (worldY / tileSize);
    }

    // ----------------------------------------------------------------
    // Chunk helpers
    // ----------------------------------------------------------------

    public ChunkPos getChunkAtTile(int tileX, int tileY) {
        validateTileBounds(tileX, tileY);
        return new ChunkPos(tileX / chunkSize, tileY / chunkSize);
    }

    public int getChunkCountX() {
        return (width + chunkSize - 1) / chunkSize;
    }

    public int getChunkCountY() {
        return (height + chunkSize - 1) / chunkSize;
    }

    public boolean isValidChunk(int chunkX, int chunkY) {
        return chunkX >= 0 && chunkX < getChunkCountX()
            && chunkY >= 0 && chunkY < getChunkCountY();
    }

    public int toChunkX(int tileX) {
        return tileX / chunkSize;
    }

    public int toChunkY(int tileY) {
        return tileY / chunkSize;
    }

    public int getChunkMinX(int chunkX) {
        return chunkX * chunkSize;
    }

    public int getChunkMinY(int chunkY) {
        return chunkY * chunkSize;
    }

    public int getChunkMaxXExclusive(int chunkX) {
        return Math.min(getChunkMinX(chunkX) + chunkSize, width);
    }

    public int getChunkMaxYExclusive(int chunkY) {
        return Math.min(getChunkMinY(chunkY) + chunkSize, height);
    }

    public boolean isTileInChunk(int tileX, int tileY, int chunkX, int chunkY) {
        if (!inBounds(tileX, tileY) || !isValidChunk(chunkX, chunkY)) {
            return false;
        }

        return tileX >= getChunkMinX(chunkX)
            && tileX < getChunkMaxXExclusive(chunkX)
            && tileY >= getChunkMinY(chunkY)
            && tileY < getChunkMaxYExclusive(chunkY);
    }

    // ----------------------------------------------------------------
    // Clamping
    // ----------------------------------------------------------------

    public int clampTileX(int tileX) {
        return Math.max(0, Math.min(tileX, width - 1));
    }

    public int clampTileY(int tileY) {
        return Math.max(0, Math.min(tileY, height - 1));
    }

    // ----------------------------------------------------------------
    // Rectangle / bounds helpers
    // ----------------------------------------------------------------

    public int getMinTileX(float worldX) {
        return clampTileX((int) (worldX / tileSize));
    }

    public int getMinTileY(float worldY) {
        return clampTileY((int) (worldY / tileSize));
    }

    public int getMaxTileXInclusive(float worldX, float widthPixels, float epsilon) {
        return clampTileX((int) ((worldX + widthPixels - epsilon) / tileSize));
    }

    public int getMaxTileYInclusive(float worldY, float heightPixels, float epsilon) {
        return clampTileY((int) ((worldY + heightPixels - epsilon) / tileSize));
    }

    public TileBounds getTileBoundsForRect(
        float worldX,
        float worldY,
        float rectWidth,
        float rectHeight,
        float epsilon
    ) {
        return new TileBounds(
            getMinTileX(worldX),
            getMinTileY(worldY),
            getMaxTileXInclusive(worldX, rectWidth, epsilon),
            getMaxTileYInclusive(worldY, rectHeight, epsilon)
        );
    }

    // ----------------------------------------------------------------
    // Iteration
    // ----------------------------------------------------------------

    public void forEachTile(TileVisitor visitor) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                visitor.visit(x, y, tiles[x][y]);
            }
        }
    }

    public void forEachInChunk(int chunkX, int chunkY, TileVisitor visitor) {
        if (!isValidChunk(chunkX, chunkY)) {
            return;
        }

        int minX = getChunkMinX(chunkX);
        int minY = getChunkMinY(chunkY);
        int maxX = getChunkMaxXExclusive(chunkX);
        int maxY = getChunkMaxYExclusive(chunkY);

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                visitor.visit(x, y, tiles[x][y]);
            }
        }
    }

    public void forEachChunk(ChunkVisitor visitor) {
        int chunkCountX = getChunkCountX();
        int chunkCountY = getChunkCountY();

        for (int chunkX = 0; chunkX < chunkCountX; chunkX++) {
            for (int chunkY = 0; chunkY < chunkCountY; chunkY++) {
                visitor.visit(
                    chunkX,
                    chunkY,
                    getChunkMinX(chunkX),
                    getChunkMinY(chunkY),
                    getChunkMaxXExclusive(chunkX),
                    getChunkMaxYExclusive(chunkY)
                );
            }
        }
    }

    public void forEachTileInRect(
        float worldX,
        float worldY,
        float rectWidth,
        float rectHeight,
        float epsilon,
        TileVisitor visitor
    ) {
        TileBounds bounds = getTileBoundsForRect(worldX, worldY, rectWidth, rectHeight, epsilon);

        for (int x = bounds.minX; x <= bounds.maxXInclusive; x++) {
            for (int y = bounds.minY; y <= bounds.maxYInclusive; y++) {
                visitor.visit(x, y, tiles[x][y]);
            }
        }
    }

    public void forEachTileInBounds(
        int minX,
        int minY,
        int maxXInclusive,
        int maxYInclusive,
        TileVisitor visitor
    ) {
        int clampedMinX = clampTileX(minX);
        int clampedMinY = clampTileY(minY);
        int clampedMaxX = clampTileX(maxXInclusive);
        int clampedMaxY = clampTileY(maxYInclusive);

        for (int x = clampedMinX; x <= clampedMaxX; x++) {
            for (int y = clampedMinY; y <= clampedMaxY; y++) {
                visitor.visit(x, y, tiles[x][y]);
            }
        }
    }

    // ----------------------------------------------------------------
    // Neighbors
    // ----------------------------------------------------------------

    public void forEachCardinalNeighbor(int x, int y, TileVisitor visitor) {
        visitIfInBounds(x, y + 1, visitor);
        visitIfInBounds(x + 1, y, visitor);
        visitIfInBounds(x, y - 1, visitor);
        visitIfInBounds(x - 1, y, visitor);
    }

    public void forEachNeighbor8(int x, int y, TileVisitor visitor) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                visitIfInBounds(x + dx, y + dy, visitor);
            }
        }
    }

    // ----------------------------------------------------------------
    // Internal helpers
    // ----------------------------------------------------------------

    private void visitIfInBounds(int x, int y, TileVisitor visitor) {
        if (inBounds(x, y)) {
            visitor.visit(x, y, tiles[x][y]);
        }
    }

    private void validateTileBounds(int x, int y) {
        if (!inBounds(x, y)) {
            throw new IndexOutOfBoundsException("Out of bounds: (" + x + ", " + y + ")");
        }
    }
}
