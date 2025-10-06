package me.arcademadness.omni_dungeon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.arcademadness.omni_dungeon.entities.Entity;

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
        Skin skin = this.getSkin();
        java.util.function.Function<Integer, com.badlogic.gdx.utils.Array<SlotWidget>> makeSlots = (count) -> {
            com.badlogic.gdx.utils.Array<SlotWidget> slots = new com.badlogic.gdx.utils.Array<>();
            for (int i = 0; i < count; i++) {
                slots.add(new SlotWidget(skin, SlotWidget.State.EMPTY, 48));
            }
            return slots;
        };

        com.badlogic.gdx.utils.Array<SlotWidget> inventorySlots = makeSlots.apply(500);

        com.badlogic.gdx.utils.Array<SlotWidget> armorSlots = new com.badlogic.gdx.utils.Array<>();
        for (int i = 0; i < 18; i++) {
            SlotWidget.State state = (i < 6) ? SlotWidget.State.EMPTY :
                (i < 12) ? SlotWidget.State.FILLED :
                    SlotWidget.State.LOCKED;
            armorSlots.add(new SlotWidget(skin, state, 48));
        }

        com.badlogic.gdx.utils.Array<SlotWidget> itemSlots = makeSlots.apply(27);
        com.badlogic.gdx.utils.Array<SlotWidget> actionSlots = makeSlots.apply(18);

        getInventorySection().setSlots(inventorySlots);
        getArmorSection().setSlots(armorSlots);
        getItemsSection().setSlots(itemSlots);
        getActionsSection().setSlots(actionSlots);

        pad(10).defaults().space(5);
        pack();
    }

    public SectionWidget getInventorySection() { return inventorySection; }
    public SectionWidget getArmorSection() { return armorSection; }
    public SectionWidget getItemsSection() { return itemsSection; }
    public SectionWidget getActionsSection() { return actionsSection; }
}
