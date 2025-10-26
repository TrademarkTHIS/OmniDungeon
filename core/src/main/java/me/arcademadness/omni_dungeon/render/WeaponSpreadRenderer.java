package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;

public class WeaponSpreadRenderer implements RenderLayer {
    private final PlayerEntity player;
    private final ShapeRenderer shape;

    private float radius = 40f;
    private float thickness = 3f;
    private float fadeOutTime = 2f;
    private float fade = 1f;
    private float lastMouseX, lastMouseY;
    private float timeSinceMouseMove = 0f;

    private float defaultSpread = 20f;

    public WeaponSpreadRenderer(PlayerEntity player, ShapeRenderer shape) {
        this.player = player;
        this.shape = shape;
        this.lastMouseX = Gdx.input.getX();
        this.lastMouseY = Gdx.input.getY();
    }

    @Override
    public void render(Camera camera) {
        int tileSize = EnvironmentConfig.get().getTileSize();
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (mx != lastMouseX || my != lastMouseY) {
            timeSinceMouseMove = 0f;
            lastMouseX = mx;
            lastMouseY = my;
        } else {
            timeSinceMouseMove += Gdx.graphics.getDeltaTime();
        }

        fade = MathUtils.clamp(1f - (timeSinceMouseMove / fadeOutTime), 0f, 1f);
        if (fade <= 0.01f) return;

        Vector2 center = getEntityCenter(player).scl(tileSize);
        Vector3 playerWorld = new Vector3(center.x, center.y, 0);

        Vector3 playerScreen = new Vector3(playerWorld);
        camera.project(playerScreen);

        float dx = mx - playerScreen.x;
        float dy = my - playerScreen.y;
        float angleToMouse = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        float halfSpread = defaultSpread / 2f;
        float startAngle = angleToMouse - halfSpread;
        float arcDegrees = defaultSpread;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(1f, 1f, 1f, fade);
        drawArc(shape, playerWorld.x, playerWorld.y, radius, startAngle, arcDegrees);

        shape.setColor(1f, 1f, 1f, fade * 0.1f);
        drawArc(shape, playerWorld.x, playerWorld.y, radius, startAngle + arcDegrees, 360 - arcDegrees);

        shape.end();
    }

    /**
     * Draws a circular arc using line segments.
     */
    private void drawArc(ShapeRenderer shape, float cx, float cy, float radius, float startAngle, float degrees) {
        int segments = (int)(radius * 0.5f);
        float angleStep = degrees / segments;

        float innerRadius = radius - thickness / 2f;
        float outerRadius = radius + thickness / 2f;

        for (int i = 0; i < segments; i++) {
            float angle1 = startAngle + i * angleStep;
            float angle2 = startAngle + (i + 1) * angleStep;

            float x1_inner = cx + MathUtils.cosDeg(angle1) * innerRadius;
            float y1_inner = cy + MathUtils.sinDeg(angle1) * innerRadius;
            float x2_inner = cx + MathUtils.cosDeg(angle2) * innerRadius;
            float y2_inner = cy + MathUtils.sinDeg(angle2) * innerRadius;

            float x1_outer = cx + MathUtils.cosDeg(angle1) * outerRadius;
            float y1_outer = cy + MathUtils.sinDeg(angle1) * outerRadius;
            float x2_outer = cx + MathUtils.cosDeg(angle2) * outerRadius;
            float y2_outer = cy + MathUtils.sinDeg(angle2) * outerRadius;

            shape.triangle(x1_inner, y1_inner, x2_inner, y2_inner, x1_outer, y1_outer);
            shape.triangle(x2_inner, y2_inner, x2_outer, y2_outer, x1_outer, y1_outer);
        }
    }

    public static Vector2 getEntityCenter(Entity entity) {
        //Rectangle collider = entity.getRootPart().getCollider();
        Location location = entity.getLocation();
        float centerX = location.x + 0.5f;
        float centerY = location.y + 0.5f;
        return new Vector2(centerX, centerY);
    }
}
