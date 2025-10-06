package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.components.Bounds;

public interface Visual {
    void render(ShapeRenderer shape, Bounds bounds);
    void renderSlice(ShapeRenderer shape, Bounds slice);
}
