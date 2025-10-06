package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.components.Bounds;

public class TextureVisual implements Visual {
    private final Texture texture;

    public TextureVisual(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void render(ShapeRenderer shape, Bounds bounds) {
        shape.setColor(1, 1, 1, 1);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void renderSlice(ShapeRenderer shape, Bounds slice) {
        shape.setColor(1, 1, 1, 1);
        shape.rect(slice.x, slice.y, slice.width, slice.height);
    }

    public Texture getTexture() {
        return texture;
    }
}
