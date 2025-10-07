package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.attributes.Armor;
import me.arcademadness.omni_dungeon.attributes.Health;

public class MobEntity extends BaseEntity {
    public MobEntity(int x, int y) {
        super(x, y);
        this.health = new Health(50);
        this.armor = new Armor(1);
    }
}
