package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.visuals.Visual;

public class EntityRenderer implements RenderLayer {
    private final Environment environment;
    private final FogRenderer fog;
    private final ShapeRenderer shape;
    private Entity player;

    public EntityRenderer(Environment environment, Entity player, FogRenderer fog, ShapeRenderer shape) {
        this.environment = environment;
        this.fog = fog;
        this.shape = shape;
        this.player = player;
    }

    public void render(Camera camera) {
        if (player == null) return;

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        int radius = fog.getRadiusTiles();
        int pxTile = (int) Math.floor(player.getLocation().getX());
        int pyTile = (int) Math.floor(player.getLocation().getY());

        int minTileX = Math.max(0, pxTile - radius);
        int maxTileX = Math.min(environment.getMap().width - 1, pxTile + radius);
        int minTileY = Math.max(0, pyTile - radius);
        int maxTileY = Math.min(environment.getMap().height - 1, pyTile + radius);

        int tileSize = EnvironmentConfig.get().getTileSize();

        float minPixelX = minTileX * tileSize;
        float maxPixelX = (maxTileX + 1) * tileSize;
        float minPixelY = minTileY * tileSize;
        float maxPixelY = (maxTileY + 1) * tileSize;

        for (Entity e : environment.getEntities()) {
            for (EntityPart part : e.getParts()) {
                Visual v = part.getVisual();
                Rectangle r = part.getCollider();

                if (v == null || r == null) continue;

                float px = r.x * tileSize;
                float py = r.y * tileSize;
                float right = px + r.width;
                float top = py + r.height;

                if (right < minPixelX || px > maxPixelX ||
                    top < minPixelY || py > maxPixelY) {
                    continue;
                }

                if (px >= minPixelX && right <= maxPixelX &&
                    py >= minPixelY && top <= maxPixelY) {
                    Rectangle pixelRect = new Rectangle(px, py, r.width, r.height);
                    v.render(shape, pixelRect, part.getWorldRotation());
                    continue;
                }

                float clippedMinX = Math.max(px, minPixelX);
                float clippedMinY = Math.max(py, minPixelY);
                float clippedMaxX = Math.min(right, maxPixelX);
                float clippedMaxY = Math.min(top, maxPixelY);

                Rectangle slice = new Rectangle(
                    clippedMinX,
                    clippedMinY,
                    clippedMaxX - clippedMinX,
                    clippedMaxY - clippedMinY
                );
                v.renderSlice(shape, slice, part.getWorldRotation());
            }
        }

        shape.end();
    }
}
