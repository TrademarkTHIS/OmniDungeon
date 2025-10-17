package me.arcademadness.omni_dungeon.listeners;

import me.arcademadness.omni_dungeon.events.*;

public class ArmorDemo implements EventListener {

    @Subscribe
    public void onEntityDamage(EntityDamageEvent e) {
        e.setDamage((int)(e.getDamage() * 0.8));
    }
}
