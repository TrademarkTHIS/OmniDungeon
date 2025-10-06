package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.Tile;
import me.arcademadness.omni_dungeon.TileMap;

public class WorldRenderer {
    private final World world;
    private final ShapeRenderer shape;
    private final FogRenderer fog;

    public WorldRenderer(World world, ShapeRenderer shape, FogRenderer fog) {
        this.world = world;
        this.shape = shape;
        this.fog = fog;
    }

    public void render(Camera camera) {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        TileMap map = world.getMap();
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                VisibilityState vis = fog.visibility[x][y];
                if (vis == VisibilityState.UNSEEN) continue; // Skip completely

                Tile t = map.tiles[x][y];
                if (t.walkable) shape.setColor(0.13f, 0.55f, 0.13f, 1f);
                else shape.setColor(0.15f, 0.15f, 0.15f, 1f);

                if (vis == VisibilityState.SEEN) shape.setColor(shape.getColor().r * 0.5f, shape.getColor().g * 0.5f, shape.getColor().b * 0.5f, 1f);

                shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
            }
        }

        shape.end();
    }
}
