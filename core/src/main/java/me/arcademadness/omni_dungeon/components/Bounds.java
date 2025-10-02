package me.arcademadness.omni_dungeon.components;

public class Bounds {
    public final float x, y, width, height;

    public Bounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Bounds other) {
        return x < other.x + other.width && x + width > other.x &&
            y < other.y + other.height && y + height > other.y;
    }
}

