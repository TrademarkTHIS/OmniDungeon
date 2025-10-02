package me.arcademadness.omni_dungeon.components;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public List<String> getItems() {
        return items;
    }
}
