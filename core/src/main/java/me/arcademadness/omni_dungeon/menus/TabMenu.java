package me.arcademadness.omni_dungeon.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.arcademadness.omni_dungeon.ui.MenuScreen;

public class TabMenu extends MenuScreen {

    static final int SLOT_SIZE = 48;
    private static final int INVENTORY_VISIBLE_ROWS = 6;
    private static final int ARMOR_VISIBLE_ROWS = 2;
    private static final int ITEMS_VISIBLE_ROWS = 2;
    private static final int ACTIONS_VISIBLE_ROWS = 2;
    private static final int COLUMNS = 9;

    private SectionWidget inventorySection;
    private SectionWidget armorSection;
    private SectionWidget itemsSection;
    private SectionWidget actionsSection;

    public TabMenu(Skin skin) {
        super(skin);

        Table container = new Table();
        container.defaults().padBottom(10).expandX().fillX().top();

        setFillParent(true);
        clearChildren();
        defaults().center();
        pad(10);

        inventorySection = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#577590"), skin); // bluish
        armorSection     = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#F9C74F"), skin); // yellow
        itemsSection     = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#43AA8B"), skin); // teal
        actionsSection   = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#FFADAD"), skin); // pink

        inventorySection.setVisibleRows(INVENTORY_VISIBLE_ROWS);
        armorSection.setVisibleRows(ARMOR_VISIBLE_ROWS);
        itemsSection.setVisibleRows(ITEMS_VISIBLE_ROWS);
        actionsSection.setVisibleRows(ACTIONS_VISIBLE_ROWS);

        container.add(inventorySection).growX().row();
        container.add(armorSection).growX().row();
        container.add(itemsSection).growX().row();
        container.add(actionsSection).growX().row();

        add(container).center().expand();
    }

    public SectionWidget getInventorySection() { return inventorySection; }
    public SectionWidget getArmorSection() { return armorSection; }
    public SectionWidget getItemsSection() { return itemsSection; }
    public SectionWidget getActionsSection() { return actionsSection; }
}
