package me.arcademadness.omni_dungeon.movement;

public class MovementIntent {
    public final float dx;
    public final float dy;

    public MovementIntent(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static MovementIntent none() {
        return new MovementIntent(0, 0);
    }
}
