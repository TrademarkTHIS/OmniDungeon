package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Rectangle;

public class Bounds extends Rectangle {

    public Bounds(float x, float y, float width, float height) {
        this.set(x,y,width,height);
    }

    public boolean intersects(Bounds other) {
        return this.overlaps(other);
    }
}

