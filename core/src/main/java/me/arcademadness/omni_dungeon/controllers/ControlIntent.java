package me.arcademadness.omni_dungeon.controllers;

public class ControlIntent {
    public final float dx;
    public final float dy;

    public ControlIntent(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static ControlIntent none() {
        return new ControlIntent(0, 0);
    }
}
