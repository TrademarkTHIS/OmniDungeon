package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.Tile;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;

public class WorldRenderer {
    private final World world;
    private final ShapeRenderer shape;

    public WorldRenderer(World world, ShapeRenderer shape) {
        this.world = world;
        this.shape = shape;
    }

    public void render(Camera camera) {
        shape.setProjectionMatrix(camera.combined);

        // Multiplied by 5, but needs to be the zoom value
        float halfW = (camera.viewportWidth * 5) / 2f;
        float halfH = (camera.viewportHeight * 5) / 2f;
        float minX = camera.position.x - halfW;
        float minY = camera.position.y - halfH;
        float maxX = camera.position.x + halfW;
        float maxY = camera.position.y + halfH;

        int startTileX = Math.max(0, (int) Math.floor(minX / TileMap.TILE_SIZE));
        int startTileY = Math.max(0, (int) Math.floor(minY / TileMap.TILE_SIZE));
        int endTileX = Math.min(world.getMap().width - 1,
            (int) Math.ceil(maxX / TileMap.TILE_SIZE));
        int endTileY = Math.min(world.getMap().height - 1,
            (int) Math.ceil(maxY / TileMap.TILE_SIZE));

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = startTileX; x <= endTileX; x++) {
            for (int y = startTileY; y <= endTileY; y++) {
                Tile t = world.getMap().tiles[x][y];
                if (t.walkable) shape.setColor(0.13f, 0.55f, 0.13f, 1f);
                else shape.setColor(0.15f, 0.15f, 0.15f, 1f);
                shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE,
                    TileMap.TILE_SIZE, TileMap.TILE_SIZE);
            }
        }

        for (Entity e : world.getEntities()) {
            // Somehow skip entities beyond the fog of war
            // Slice entity sprite when entering fog of war
            // May need to pass Fog Renderer in to do this
            e.render(shape);
        }

        shape.end();
    }
}
