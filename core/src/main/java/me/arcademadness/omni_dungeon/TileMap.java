package me.arcademadness.omni_dungeon;

public class TileMap {
    public static final int TILE_SIZE = 32;
    public Tile[][] tiles;
    public int width, height;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                boolean isBorder = x == 0 || y == 0 || x == width-1 || y == height-1;
                tiles[x][y] = new Tile(!isBorder);
            }
        }
    }
}

