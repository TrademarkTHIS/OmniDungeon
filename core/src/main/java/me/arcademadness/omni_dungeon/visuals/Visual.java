package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public interface Visual {
    void render(ShapeRenderer shape, Rectangle bounds, float worldRotation);
    void renderSlice(ShapeRenderer shape, Rectangle slice, float worldRotation);
}
