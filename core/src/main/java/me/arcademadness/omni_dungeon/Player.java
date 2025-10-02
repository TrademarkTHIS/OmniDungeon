package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    public int x, y;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void render(ShapeRenderer shape) {
        shape.setColor(Color.SKY);
        shape.rect(
            x * TileMap.TILE_SIZE,
            y * TileMap.TILE_SIZE,
            TileMap.TILE_SIZE,
            TileMap.TILE_SIZE
        );
    }
}
