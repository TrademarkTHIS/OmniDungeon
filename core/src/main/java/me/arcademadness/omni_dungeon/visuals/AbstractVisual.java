package me.arcademadness.omni_dungeon.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.components.Bounds;

public abstract class AbstractVisual implements Visual {
    protected Color color;

    public AbstractVisual(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public abstract void render(ShapeRenderer shape, Bounds bounds);

    @Override
    public abstract void renderSlice(ShapeRenderer shape, Bounds slice);
}
