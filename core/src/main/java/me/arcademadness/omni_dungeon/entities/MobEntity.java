package me.arcademadness.omni_dungeon.entities;

import me.arcademadness.omni_dungeon.attributes.Armor;
import me.arcademadness.omni_dungeon.attributes.Health;

public class MobEntity extends BaseEntity {
    public MobEntity() {
        super();
        this.health = new Health(50);
        this.armor = new Armor(1);
    }
}
