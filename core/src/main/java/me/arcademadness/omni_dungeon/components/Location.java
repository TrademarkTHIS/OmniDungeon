package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Vector2;

public class Location extends Vector2 {
    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }
}
