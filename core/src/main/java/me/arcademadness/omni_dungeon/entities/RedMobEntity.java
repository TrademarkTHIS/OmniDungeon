package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;

public class RedMobEntity extends MobEntity {

    public RedMobEntity(int x, int y) {
        super(x, y);

        ShapeVisual visual = new ShapeVisual(Color.RED);
        Rectangle collider = new Rectangle(0, 0, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
        EntityPart part = new EntityPart(this, visual, collider);

        this.getParts().add(part);
    }
}
