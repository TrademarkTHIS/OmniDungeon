package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.visuals.Visual;

public class EntityRenderer implements RenderLayer {
    private final EnvironmentView environment;
    private final FogRenderer fog;
    private final ShapeRenderer shape;
    private Entity target;

    public EntityRenderer(EnvironmentView environment, Entity target, FogRenderer fog, ShapeRenderer shape) {
        this.environment = environment;
        this.fog = fog;
        this.shape = shape;
        this.target = target;
    }

    public void render(Camera camera) {
        if (target == null) return;

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        int radius = fog.getRadiusTiles();
        int pxTile = (int) Math.floor(target.getLocation().getX());
        int pyTile = (int) Math.floor(target.getLocation().getY());

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
            EntityPart root = e.getRootPart();
            if (root == null) continue;

            root.forEachPart(part -> {
                Visual v = part.getVisual();
                Rectangle r = part.getCollider();

                if (v == null || r == null) return;

                float px = r.x;
                float py = r.y;
                float right = px + r.width;
                float top = py + r.height;

                if (right < minPixelX || px > maxPixelX ||
                    top < minPixelY || py > maxPixelY) {
                    return;
                }

                if (px >= minPixelX && right <= maxPixelX &&
                    py >= minPixelY && top <= maxPixelY) {
                    Rectangle pixelRect = new Rectangle(px, py, r.width, r.height);
                    v.render(shape, pixelRect, part.getWorldRotation());
                    return;
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
            });
        }

        shape.end();
    }
}
