package me.arcademadness.omni_dungeon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.arcademadness.omni_dungeon.attributes.Armor;
import me.arcademadness.omni_dungeon.attributes.Health;
import me.arcademadness.omni_dungeon.TileMap;

public class MobEntity extends BaseEntity {
    public MobEntity(int x, int y) {
        super(x, y);
        this.health = new Health(50);
        this.armor = new Armor(1);
    }
}
