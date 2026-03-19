package me.arcademadness.omni_dungeon.environment.world;

import com.badlogic.gdx.math.Vector2;

public class FlowField {

    private final Vector2[][] directions;
    private final float[][] integration;

    public final int width;
    public final int height;

    public FlowField(int width, int height) {

        this.width = width;
        this.height = height;

        directions = new Vector2[width][height];
        integration = new float[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                directions[x][y] = new Vector2();
                integration[x][y] = Float.POSITIVE_INFINITY;

            }
        }
    }

    public Vector2 getDirection(int x, int y) {
        return directions[x][y];
    }

    public void setDirection(int x, int y, Vector2 dir) {
        directions[x][y].set(dir);
    }

    public float getCost(int x, int y) {
        return integration[x][y];
    }

    public void setCost(int x, int y, float cost) {
        integration[x][y] = cost;
    }
}
