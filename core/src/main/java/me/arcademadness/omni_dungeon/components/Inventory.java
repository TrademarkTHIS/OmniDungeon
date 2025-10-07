package me.arcademadness.omni_dungeon.components;

import me.arcademadness.omni_dungeon.attributes.MaxActionSlots;
import me.arcademadness.omni_dungeon.attributes.MaxArmorSlots;
import me.arcademadness.omni_dungeon.attributes.MaxItemSlots;
import me.arcademadness.omni_dungeon.items.Item;
import me.arcademadness.omni_dungeon.items.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {

    private class InventorySlot {
        private final int index;
        private Item item;

        public InventorySlot(int index) {
            this.index = index;
        }

        public int getIndex() { return index; }
        public Item getItem() { return item; }
        public void setItem(Item item) { this.item = item; }
        public boolean isEmpty() { return item == null; }
    }

    // Stored items
    private final List<Item> items = new ArrayList<>();

    // Equipped items
    private final List<Item> equippedItems = new ArrayList<>();
    private final List<Item> equippedArmor = new ArrayList<>();

    // Slot attributes
    private final MaxItemSlots maxItemSlots;
    private final MaxArmorSlots maxArmorSlots;
    private final MaxActionSlots maxActionSlots;

    public Inventory(int maxArmorSlots, int maxItemSlots, int maxActionSlots) {
        this.maxItemSlots = new MaxItemSlots(maxItemSlots);
        this.maxArmorSlots = new MaxArmorSlots(maxArmorSlots);
        this.maxActionSlots = new MaxActionSlots(maxActionSlots);
    }

    public void addItem(Item item) {
        if (item != null && !contains(item)) {
            items.add(item);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
        equippedItems.remove(item);
        equippedArmor.remove(item);
    }

    public Item removeItem(int index) {
        Item removed = items.remove(index);
        equippedItems.remove(removed);
        equippedArmor.remove(removed);
        return removed;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }

    public Item get(int index) {
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    public void clear() {
        items.clear();
        equippedItems.clear();
        equippedArmor.clear();
    }

    public boolean equip(Item item) {
        if (item == null || !items.contains(item)) return false;

        unequip(item);

        if (item.getType() == ItemType.ARMOR) {
            if (equippedArmor.size() >= maxArmorSlots.getFinalValue()) {
                return false;
            }
            equippedArmor.add(item);
        } else {
            if (equippedItems.size() >= maxItemSlots.getFinalValue()) {
                return false;
            }
            equippedItems.add(item);
        }
        return true;
    }

    public boolean unequip(Item item) {
        return equippedItems.remove(item) || equippedArmor.remove(item);
    }

    public boolean isEquipped(Item item) {
        return equippedItems.contains(item) || equippedArmor.contains(item);
    }

    private boolean contains(Item item) {
        return items.contains(item) || equippedItems.contains(item) || equippedArmor.contains(item);
    }

    public List<Item> getEquippedItems() {
        return Collections.unmodifiableList(equippedItems);
    }

    public List<Item> getEquippedArmor() {
        return Collections.unmodifiableList(equippedArmor);
    }

    public MaxItemSlots getMaxItemSlots() {
        return maxItemSlots;
    }

    public MaxArmorSlots getMaxArmorSlots() {
        return maxArmorSlots;
    }

    public MaxActionSlots getMaxActionSlots() {
        return maxActionSlots;
    }
}
