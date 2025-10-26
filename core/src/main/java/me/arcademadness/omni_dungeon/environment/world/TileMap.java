package me.arcademadness.omni_dungeon.environment.world;

import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;

public class TileMap {
    private final int tileSize;
    public final Tile[][] tiles;
    public final int width, height;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tileSize = EnvironmentConfig.get().getTileSize();
        tiles = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean isBorder = x == 0 || y == 0 || x == width - 1 || y == height - 1;
                tiles[x][y] = new Tile(!isBorder);
            }
        }
    }

    public int getTileSize() {
        return tileSize;
    }
}


