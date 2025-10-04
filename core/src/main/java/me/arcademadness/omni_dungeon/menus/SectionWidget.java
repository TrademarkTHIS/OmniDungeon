package me.arcademadness.omni_dungeon.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class SectionWidget extends Table {
    protected Label title;
    protected Table grid;
    protected ScrollPane scrollPane;
    protected int columns;
    protected int slotSize;

    public SectionWidget(String titleText, int columns, int slotSize, int visibleRows, boolean scrollable, Skin skin) {
        this.columns = columns;
        this.slotSize = slotSize;

        title = new Label(titleText, skin);
        add(title).left().padBottom(4).row();

        grid = new Table();
        grid.defaults().size(slotSize);

        if (scrollable) {
            scrollPane = new ScrollPane(grid, skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setScrollbarsVisible(false);
            add(scrollPane).height(visibleRows * slotSize).growX();
        } else {
            add(grid).height(visibleRows * slotSize).growX();
        }
    }

    public void setSlots(Array<SlotWidget> slots) {
        grid.clearChildren();
        for (int i = 0; i < slots.size; i++) {
            grid.add(slots.get(i));
            if ((i + 1) % columns == 0) {
                grid.row();
            }
        }
    }
}
