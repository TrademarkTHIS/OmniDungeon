package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class TextureVisual implements Visual {
    private final Texture texture;

    public TextureVisual(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void render(ShapeRenderer shape, Rectangle bounds, float rotation) {
        shape.setColor(1, 1, 1, 1);

        float originX = bounds.width * 0.5f;
        float originY = bounds.height * 0.5f;
        shape.rect(bounds.x, bounds.y, originX, originY, bounds.width, bounds.height, 1f, 1f, rotation);
    }

    @Override
    public void renderSlice(ShapeRenderer shape, Rectangle slice, float rotation) {
        shape.setColor(1, 1, 1, 1);

        float originX = slice.width * 0.5f;
        float originY = slice.height * 0.5f;
        shape.rect(slice.x, slice.y, originX, originY, slice.width, slice.height, 1f, 1f, rotation);
    }

    public Texture getTexture() {
        return texture;
    }
}
