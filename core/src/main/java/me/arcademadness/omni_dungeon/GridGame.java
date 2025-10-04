package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.arcademadness.omni_dungeon.controllers.MobController;
import me.arcademadness.omni_dungeon.controllers.PlayerController;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;
import me.arcademadness.omni_dungeon.menus.SlotWidget;
import me.arcademadness.omni_dungeon.menus.TabMenu;

public class GridGame extends ApplicationAdapter {
    private ShapeRenderer shape;
    private World world;
    private PlayerEntity player;
    private PlayerController playerController;
    private TabMenu tabMenu;
    private Stage stage;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        TileMap map = new TileMap(10, 10);
        world = new World(map);

        player = new PlayerEntity(map.width / 2, map.height / 2);
        playerController = new PlayerController();
        player.setController(playerController);
        world.addEntity(player);

        MobEntity redMob = new MobEntity(3, 3);
        redMob.setController(new MobController());
        world.addEntity(redMob);

        Skin skin = new Skin(Gdx.files.absolute("assets/uiskin.json"));

        tabMenu = new TabMenu(skin);
        tabMenu.setColor(1f, 1f, 1f, 0.85f);

        populateTabMenu(tabMenu, skin);

        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        root.add(tabMenu).center();
        stage.addActor(root);

        tabMenu.setVisible(false);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        world.tick(delta);

        ScreenUtils.clear(Color.BLACK);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < world.getMap().width; x++) {
            for (int y = 0; y < world.getMap().height; y++) {
                Tile t = world.getMap().tiles[x][y];
                shape.setColor(t.walkable ? Color.FOREST : Color.DARK_GRAY);
                shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE,
                    TileMap.TILE_SIZE, TileMap.TILE_SIZE);
            }
        }
        for (Entity e : world.getEntities()) {
            e.render(shape);
        }
        shape.end();

        shape.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.getBatch().setColor(Color.WHITE);
        stage.getRoot().setColor(Color.WHITE);


        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            playerController.toggleMenu();
            tabMenu.setVisible(playerController.isMenuOpen());
        }

        stage.act(delta);
        stage.draw();
    }

    private void populateTabMenu(TabMenu tabMenu, Skin skin) {
        // Helper to make slots easily
        java.util.function.Function<Integer, Array<SlotWidget>> makeSlots = (count) -> {
            Array<SlotWidget> arr = new Array<>();
            for (int i = 0; i < count; i++) {
                arr.add(new SlotWidget(skin, SlotWidget.State.EMPTY, 48));
            }
            return arr;
        };

        // Inventory: 54 empty
        Array<SlotWidget> inventorySlots = makeSlots.apply(500);

        // Armor: 6 empty, 6 filled, 6 locked
        Array<SlotWidget> armorSlots = new Array<>();
        for (int i = 0; i < 18; i++) {
            SlotWidget.State state = i < 6 ? SlotWidget.State.EMPTY
                : i < 12 ? SlotWidget.State.FILLED
                : SlotWidget.State.LOCKED;
            armorSlots.add(new SlotWidget(skin, state, 48));
        }

        // Items: 18 filled
        Array<SlotWidget> itemsSlots = new Array<>();
        for (int i = 0; i < 27; i++) {
            itemsSlots.add(new SlotWidget(skin, SlotWidget.State.FILLED, 48));
        }

        // Actions: 18 empty
        Array<SlotWidget> actionsSlots = makeSlots.apply(18);

        // Assign to menu
        tabMenu.getInventorySection().setSlots(inventorySlots);
        tabMenu.getArmorSection().setSlots(armorSlots);
        tabMenu.getItemsSection().setSlots(itemsSlots);
        tabMenu.getActionsSection().setSlots(actionsSlots);

        // Optionally space them out a bit more for visual clarity
        tabMenu.pad(10).defaults().space(5);

        tabMenu.pack();
    }


    @Override
    public void dispose() {
        shape.dispose();
        stage.dispose();
        tabMenu.clear();
    }
}
