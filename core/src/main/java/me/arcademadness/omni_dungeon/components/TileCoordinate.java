package me.arcademadness.omni_dungeon.components;

public final class TileCoordinate {
    public final int x;
    public final int y;

    public TileCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TileCoordinate)) return false;
        TileCoordinate t = (TileCoordinate) o;
        return x == t.x && y == t.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "TileCoordinate[" + x + "," + y + "]";
    }
}
