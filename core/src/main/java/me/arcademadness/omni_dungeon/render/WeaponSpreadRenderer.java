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

    // Configurable parameters
    private float radius = 40f;       // distance from player
    private float thickness = 3f;     // line width
    private float fadeOutTime = 2f;   // seconds to fully fade after no mouse movement
    private float fade = 1f;          // current fade (0..1)
    private float lastMouseX, lastMouseY;
    private float timeSinceMouseMove = 0f;

    private float defaultSpread = 20f; // degrees

    public WeaponSpreadRenderer(PlayerEntity player, ShapeRenderer shape) {
        this.player = player;
        this.shape = shape;
        this.lastMouseX = Gdx.input.getX();
        this.lastMouseY = Gdx.input.getY();
    }

    @Override
    public void render(Camera camera) {
        int tileSize = EnvironmentConfig.get().getTileSize();
        // Track mouse movement
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY(); // invert Y (LibGDX origin is bottom-left)

        if (mx != lastMouseX || my != lastMouseY) {
            timeSinceMouseMove = 0f;
            lastMouseX = mx;
            lastMouseY = my;
        } else {
            timeSinceMouseMove += Gdx.graphics.getDeltaTime();
        }

        fade = MathUtils.clamp(1f - (timeSinceMouseMove / fadeOutTime), 0f, 1f);
        if (fade <= 0.01f) return;

        // --- Player position projected to screen space ---
        Vector2 center = getEntityCenter(player).scl(tileSize);
        Vector3 playerWorld = new Vector3(center.x, center.y, 0);


        Vector3 playerScreen = new Vector3(playerWorld);
        camera.project(playerScreen); // convert to screen coordinates

        // --- Compute angle to mouse (screen space) ---
        float dx = mx - playerScreen.x;
        float dy = my - playerScreen.y;
        float angleToMouse = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        float halfSpread = defaultSpread / 2f;
        float startAngle = angleToMouse - halfSpread;
        float arcDegrees = defaultSpread;

        // --- Draw ---
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setProjectionMatrix(camera.combined);

        // Main arc
        shape.setColor(1f, 1f, 1f, fade);
        drawArc(shape, playerWorld.x, playerWorld.y, radius, startAngle, arcDegrees);

        // Faded remainder
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

        // Simulated line thickness by drawing multiple concentric arcs
        int thicknessSteps = (int) thickness; // e.g., if thickness=4, draw 4 arcs outward
        for (int t = 0; t < thicknessSteps; t++) {
            float r = radius + t - (thicknessSteps / 2f); // center the thickness evenly
            float prevX = cx + MathUtils.cosDeg(startAngle) * r;
            float prevY = cy + MathUtils.sinDeg(startAngle) * r;

            for (int i = 1; i <= segments; i++) {
                float angle = startAngle + i * angleStep;
                float x = cx + MathUtils.cosDeg(angle) * r;
                float y = cy + MathUtils.sinDeg(angle) * r;
                shape.line(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
            }
        }
    }

    // Utility to get combined bounding box and center
    public static Vector2 getEntityCenter(Entity entity) {
        //Rectangle collider = entity.getRootPart().getCollider();
        Location location = entity.getLocation();
        float centerX = location.x + 0.5f;
        float centerY = location.y + 0.5f;
        return new Vector2(centerX, centerY);
    }
}
