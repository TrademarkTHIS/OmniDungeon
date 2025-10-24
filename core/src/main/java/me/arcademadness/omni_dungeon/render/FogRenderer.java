package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.world.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;

public class FogRenderer implements RenderLayer {
    private final Environment environment;
    private final ShapeRenderer shape;
    public final VisibilityState[][] visibility;
    private int radiusTiles;

    private float unseenAlpha = 1.0f;
    private float seenAlpha = 0.05f;

    private Entity player;

    public FogRenderer(Environment environment, Entity player, ShapeRenderer shape, int radiusTiles) {
        this.environment = environment;
        this.shape = shape;
        this.radiusTiles = radiusTiles;
        this.player = player;

        TileMap map = environment.getMap();
        visibility = new VisibilityState[map.width][map.height];
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                visibility[x][y] = VisibilityState.UNSEEN;
            }
        }
    }

    public void render(Camera camera) {
        int tileSize = EnvironmentConfig.get().getTileSize();

        if (player == null) return;
        shape.setProjectionMatrix(camera.combined);

        TileMap map = environment.getMap();
        int playerTileX = (int) player.getLocation().getX();
        int playerTileY = (int) player.getLocation().getY();

        // SEEN tiles
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                if (visibility[x][y] == VisibilityState.VISIBLE) {
                    visibility[x][y] = VisibilityState.SEEN;
                }
            }
        }

        // VISIBLE tiles
        for (int x = Math.max(0, playerTileX - radiusTiles); x <= Math.min(map.width - 1, playerTileX + radiusTiles); x++) {
            for (int y = Math.max(0, playerTileY - radiusTiles); y <= Math.min(map.height - 1, playerTileY + radiusTiles); y++) {
                int dx = Math.abs(x - playerTileX);
                int dy = Math.abs(y - playerTileY);
                int dist = Math.max(dx, dy);
                if (dist <= radiusTiles) {
                    visibility[x][y] = VisibilityState.VISIBLE;
                }
            }
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                VisibilityState state = visibility[x][y];
                if (state == VisibilityState.UNSEEN) {
                    shape.setColor(0, 0, 0, unseenAlpha);
                    shape.rect(x * tileSize, y * tileSize, tileSize, tileSize);
                } else if (state == VisibilityState.SEEN) {
                    shape.setColor(0, 0, 0, seenAlpha);
                    shape.rect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }
        shape.end();
    }

    public int getRadiusTiles() {
        return radiusTiles;
    }

    public void revealAll() {
        TileMap map = environment.getMap();
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                visibility[x][y] = VisibilityState.VISIBLE;
            }
        }
    }
}
