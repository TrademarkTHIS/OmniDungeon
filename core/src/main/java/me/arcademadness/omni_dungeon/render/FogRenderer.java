package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;

public class FogRenderer {
    private final World world;
    private final ShapeRenderer shape;
    private final int[][] visibility;
    private int radiusTiles = 6;
    private float unseenAlpha = 1.0f;
    private float seenAlpha = 0.6f;

    public FogRenderer(World world, ShapeRenderer shape, int radiusTiles) {
        this.world = world;
        this.shape = shape;
        this.radiusTiles = radiusTiles;

        TileMap map = world.getMap();
        visibility = new int[map.width][map.height];
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                visibility[x][y] = 0;
            }
        }
    }

    public void render(Camera camera, PlayerEntity player) {
        if (player == null) return;
        shape.setProjectionMatrix(camera.combined);

        TileMap map = world.getMap();
        int playerTileX = (int) player.getLocation().getX();
        int playerTileY = (int) player.getLocation().getY();

        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                if (visibility[x][y] == 2) visibility[x][y] = 1;
            }
        }

        for (int x = Math.max(0, playerTileX - radiusTiles); x <= Math.min(map.width - 1, playerTileX + radiusTiles); x++) {
            for (int y = Math.max(0, playerTileY - radiusTiles); y <= Math.min(map.height - 1, playerTileY + radiusTiles); y++) {
                int dx = Math.abs(x - playerTileX);
                int dy = Math.abs(y - playerTileY);
                int dist = Math.max(dx, dy);
                if (dist <= radiusTiles) {
                    visibility[x][y] = 2;
                }
            }
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                int state = visibility[x][y];
                if (state == 0) {
                    shape.setColor(0, 0, 0, unseenAlpha);
                    shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                } else if (state == 1) {
                    shape.setColor(0, 0, 0, seenAlpha);
                    shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                }
            }
        }
        shape.end();
    }

    public void revealAll() {
        TileMap map = world.getMap();
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                visibility[x][y] = 2;
            }
        }
    }
}
