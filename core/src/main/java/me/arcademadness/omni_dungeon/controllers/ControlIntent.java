package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.actions.Action;
import java.util.ArrayList;
import java.util.List;

public class ControlIntent {
    private final List<Action> actions = new ArrayList<>();

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

    public static ControlIntent none() {
        return new ControlIntent();
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }
}
