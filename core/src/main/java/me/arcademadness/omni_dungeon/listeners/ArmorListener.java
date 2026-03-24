package me.arcademadness.omni_dungeon.listeners;

import me.arcademadness.omni_dungeon.events.*;
import me.arcademadness.omni_dungeon.events.entity.EntityDamageEvent;

/***
 * Pretty much a demo for testing the listener system.
 * It's probably better to do the armor calculation in the damage event itself.
 */
public class ArmorListener implements EventListener {

    @Subscribe
    public void onEntityDamage(EntityDamageEvent event) {
        int armor = event.getEntity().getArmor().getFinalValue();
        if (armor <= 0) return;

        int damage = event.getDamage();
        int newDamage = Math.max(damage - armor, 0);
        //System.out.println("Old Damage: " + damage + " | New Damage:" + newDamage);
        event.setDamage(newDamage);
    }
}
