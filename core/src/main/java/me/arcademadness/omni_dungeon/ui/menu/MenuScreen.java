package me.arcademadness.omni_dungeon.ui.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class MenuScreen extends Table {
    public MenuScreen(Skin skin) {
        super(skin);
        setFillParent(true);
        defaults().center();
        setVisible(false);
    }
}
