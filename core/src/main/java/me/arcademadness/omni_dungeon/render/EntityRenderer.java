package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


import me.arcademadness.omni_dungeon.World;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.components.Bounds;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;
import me.arcademadness.omni_dungeon.visuals.Visual;

public class EntityRenderer {
    private final World world;
    private final FogRenderer fog;
    private final ShapeRenderer shape;

    public EntityRenderer(World world, FogRenderer fog, ShapeRenderer shape) {
        this.world = world;
        this.fog = fog;
        this.shape = shape;
    }

    public void render(Camera camera, PlayerEntity player) {
        if (player == null) return;

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        int radius = fog.getRadiusTiles();
        int px = (int) player.getLocation().getX();
        int py = (int) player.getLocation().getY();

        int minTileX = Math.max(0, px - radius);
        int maxTileX = Math.min(world.getMap().width - 1, px + radius);
        int minTileY = Math.max(0, py - radius);
        int maxTileY = Math.min(world.getMap().height - 1, py + radius);

        for (Entity e : world.getEntities()) {
            Bounds b = e.getBounds();
            Visual v = e.getVisual();
            if (v == null) continue;

            float tileX = b.x / TileMap.TILE_SIZE;
            float tileY = b.y / TileMap.TILE_SIZE;
            float tileW = b.width / TileMap.TILE_SIZE;
            float tileH = b.height / TileMap.TILE_SIZE;

            float tileRight = tileX + tileW;
            float tileTop   = tileY + tileH;

            if (tileRight < minTileX || tileX > maxTileX + 1 ||
                tileTop < minTileY || tileY > maxTileY + 1)
                continue;

            if (tileX >= minTileX && tileRight <= maxTileX + 1 &&
                tileY >= minTileY && tileTop <= maxTileY + 1) {
                v.render(shape, b);
                continue;
            }

            float clippedMinX = Math.max(tileX, minTileX);
            float clippedMinY = Math.max(tileY, minTileY);
            float clippedMaxX = Math.min(tileRight, maxTileX + 1);
            float clippedMaxY = Math.min(tileTop, maxTileY + 1);

            Bounds slice = new Bounds(
                clippedMinX * TileMap.TILE_SIZE,
                clippedMinY * TileMap.TILE_SIZE,
                (clippedMaxX - clippedMinX) * TileMap.TILE_SIZE,
                (clippedMaxY - clippedMinY) * TileMap.TILE_SIZE
            );

            v.renderSlice(shape, slice);
        }

        shape.end();
    }
}

