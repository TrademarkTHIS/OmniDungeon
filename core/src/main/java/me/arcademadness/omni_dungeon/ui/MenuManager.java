package me.arcademadness.omni_dungeon.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private final Stage stage;
    private final Map<String, MenuScreen> menus = new HashMap<>();
    private MenuScreen active;

    public MenuManager(Stage stage) {
        this.stage = stage;
    }

    public void register(String id, MenuScreen menu) {
        menus.put(id, menu);
        if (menu.getParent() == null) {
            stage.addActor(menu);
        }
        menu.setVisible(false);
    }

    public void show(String id) {
        hideAll();
        MenuScreen m = menus.get(id);
        if (m != null) {
            m.setVisible(true);
            active = m;
        }
    }

    public void hideAll() {
        if (active != null) active.setVisible(false);
        for (MenuScreen m : menus.values()) {
            m.setVisible(false);
        }
        active = null;
    }

    public MenuScreen getActive() {
        return active;
    }
}
