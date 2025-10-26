package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Vector2;

public class Location extends Vector2 {
    public Location(float x, float y) {
        super(x, y);
    }

    public Location() {
        super(0,0);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public int tileX() { return (int) Math.floor(x); }
    public int tileY() { return (int) Math.floor(y); }

    public void set(Location loc) {
        this.x = loc.x;
        this.y = loc.y;
    }
}

