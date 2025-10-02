package me.arcademadness.omni_dungeon.attributes;

public class Health {
    private int current;
    private final int max;

    public Health(int max) {
        this.max = max;
        this.current = max;
    }

    public int getCurrent() { return current; }
    public int getMax() { return max; }

    public void damage(int amount) {
        current = Math.max(0, current - amount);
    }

    public void heal(int amount) {
        current = Math.min(max, current + amount);
    }

    public boolean isAlive() {
        return current > 0;
    }
}
