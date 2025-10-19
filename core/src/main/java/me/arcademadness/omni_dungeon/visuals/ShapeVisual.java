package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ShapeVisual extends AbstractVisual {
    public ShapeVisual(Color color) {
        super(color);
    }

    @Override
    public void render(ShapeRenderer shape, Rectangle bounds, float rotation) {
        shape.setColor(color);

        float originX = bounds.width * 0.5f;
        float originY = bounds.height * 0.5f;

        shape.rect(bounds.x, bounds.y, originX, originY, bounds.width, bounds.height, 1f, 1f, rotation);
    }

    @Override
    public void renderSlice(ShapeRenderer shape, Rectangle slice, float rotation) {
        shape.setColor(color);

        float originX = slice.width * 0.5f;
        float originY = slice.height * 0.5f;
        shape.rect(slice.x, slice.y, originX, originY, slice.width, slice.height, 1f, 1f, rotation);
    }
}
