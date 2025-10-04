package me.arcademadness.omni_dungeon.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TabMenu extends Table {

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
        super();
        pad(10);
        defaults().padBottom(10);
        defaults().expandX().fillX().top();

        inventorySection = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#577590"), skin); // bluish
        armorSection     = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#F9C74F"), skin); // yellow
        itemsSection     = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#43AA8B"), skin); // teal
        actionsSection   = new SectionWidget(COLUMNS, SLOT_SIZE, Color.valueOf("#FFADAD"), skin); // pink

        inventorySection.setVisibleRows(INVENTORY_VISIBLE_ROWS);
        armorSection.setVisibleRows(ARMOR_VISIBLE_ROWS);
        itemsSection.setVisibleRows(ITEMS_VISIBLE_ROWS);
        actionsSection.setVisibleRows(ACTIONS_VISIBLE_ROWS);

        add(inventorySection).growX().row();
        add(armorSection).growX().row();
        add(itemsSection).growX().row();
        add(actionsSection).growX().row();
    }

    public SectionWidget getInventorySection() { return inventorySection; }
    public SectionWidget getArmorSection() { return armorSection; }
    public SectionWidget getItemsSection() { return itemsSection; }
    public SectionWidget getActionsSection() { return actionsSection; }
}
