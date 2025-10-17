package me.arcademadness.omni_dungeon.listeners;

import me.arcademadness.omni_dungeon.events.*;
import me.arcademadness.omni_dungeon.events.entity.EntityDamageEntityEvent;

public class ArmorDemo implements EventListener {

    @Subscribe
    public void onEntityDamage(EntityDamageEntityEvent e) {
        e.setDamage((int)(e.getDamage() * 0.8));
    }
}
