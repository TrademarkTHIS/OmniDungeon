package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.components.Bounds;

public class ShapeVisual extends AbstractVisual {
    public ShapeVisual(Color color) {
        super(color);
    }

    @Override
    public void render(ShapeRenderer shape, Bounds bounds) {
        shape.setColor(color);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void renderSlice(ShapeRenderer shape, Bounds slice) {
        shape.setColor(color);
        shape.rect(slice.x, slice.y, slice.width, slice.height);
    }
}
