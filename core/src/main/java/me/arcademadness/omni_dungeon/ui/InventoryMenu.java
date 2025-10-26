package me.arcademadness.omni_dungeon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.arcademadness.omni_dungeon.components.Inventory;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.items.Item;

public class InventoryMenu extends MenuScreen {

    private Entity player;

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

    public InventoryMenu(Skin skin, Entity player) {
        super(skin);

        this.player = player;

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

        setColor(1f, 1f, 1f, 0.95f);
    }

    public void populate() {
        Skin skin = getSkin();
        Inventory inv = player.getInventory();

        populateInventorySection(inv, 6);

        populateCappedSection(
            armorSection,
            inv.getMaxArmorSlots().getFinalValue(),
            ARMOR_VISIBLE_ROWS
        );

        populateCappedSection(
            itemsSection,
            inv.getMaxItemSlots().getFinalValue(),
            ITEMS_VISIBLE_ROWS
        );

        populateCappedSection(
            actionsSection,
            inv.getMaxActionSlots().getFinalValue(),
            ACTIONS_VISIBLE_ROWS
        );

        pad(10).defaults().space(5);
        pack();
    }

    private void populateInventorySection(Inventory inv, int visibleRows) {
        Skin skin = getSkin();
        com.badlogic.gdx.utils.Array<SlotWidget> slots = new com.badlogic.gdx.utils.Array<>();

        int itemCount = inv.size();
        int usedRows = (int) Math.ceil(itemCount / (float) COLUMNS);

        int totalRows = Math.max(visibleRows, usedRows);
        int totalSlots = totalRows * COLUMNS;

        for (int i = 0; i < totalSlots; i++) {
            if (i < itemCount) {
                Item item = inv.get(i);
                SlotWidget slot = new SlotWidget(skin, SlotWidget.State.FILLED, SLOT_SIZE);
                slot.setItemTexture(item.getTexture());
                slots.add(slot);
            } else {
                SlotWidget slot = new SlotWidget(skin, SlotWidget.State.EMPTY, SLOT_SIZE);
                slots.add(slot);
            }
        }

        inventorySection.setVisibleRows(visibleRows);
        inventorySection.setSlots(slots);
    }



    private void populateCappedSection(SectionWidget section, int maxSlots, int visibleRows) {
        Skin skin = getSkin();
        com.badlogic.gdx.utils.Array<SlotWidget> slots = new com.badlogic.gdx.utils.Array<>();

        // --- Compute how many total slots we need to fill complete rows ---
        int totalSlots = Math.max(maxSlots, COLUMNS * visibleRows);
        int remainder = totalSlots % COLUMNS;
        if (remainder != 0) {
            totalSlots += (COLUMNS - remainder); // pad to full row width
        }

        // --- Populate ---
        for (int i = 0; i < totalSlots; i++) {
            SlotWidget.State state = (i < maxSlots)
                ? SlotWidget.State.EMPTY
                : SlotWidget.State.LOCKED;
            slots.add(new SlotWidget(skin, state, SLOT_SIZE));
        }

        // --- Apply fixed visible height (scrolls if overflow) ---
        section.setVisibleRows(visibleRows);
        section.setSlots(slots);
    }

    public SectionWidget getInventorySection() { return inventorySection; }
    public SectionWidget getArmorSection() { return armorSection; }
    public SectionWidget getItemsSection() { return itemsSection; }
    public SectionWidget getActionsSection() { return actionsSection; }
}
