package me.arcademadness.omni_dungeon.menus;

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
        defaults().expandX().fillX().top();

        inventorySection = new SectionWidget("Inventory", COLUMNS, SLOT_SIZE, INVENTORY_VISIBLE_ROWS, true, skin);
        armorSection     = new SectionWidget("Armor", COLUMNS, SLOT_SIZE, ARMOR_VISIBLE_ROWS, true, skin);
        itemsSection     = new SectionWidget("Items", COLUMNS, SLOT_SIZE, ITEMS_VISIBLE_ROWS, true, skin);
        actionsSection   = new SectionWidget("Actions", COLUMNS, SLOT_SIZE, ACTIONS_VISIBLE_ROWS, false, skin);

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
