package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
        Array<SlotWidget> inventorySlots = new Array<>();
        Array<SlotWidget> armorSlots = new Array<>();
        Array<SlotWidget> itemsSlots = new Array<>();
        Array<SlotWidget> actionsSlots = new Array<>();

        for (int i = 0; i < 54; i++) {
            inventorySlots.add(new SlotWidget(skin, SlotWidget.State.EMPTY, 48));
        }

        for (int i = 0; i < 18; i++) {
            armorSlots.add(new SlotWidget(skin, SlotWidget.State.LOCKED, 48));
        }

        for (int i = 0; i < 18; i++) {
            itemsSlots.add(new SlotWidget(skin, SlotWidget.State.FILLED, 48));
        }

        for (int i = 0; i < 18; i++) {
            actionsSlots.add(new SlotWidget(skin, SlotWidget.State.EMPTY, 48));
        }

        tabMenu.getInventorySection().setSlots(inventorySlots);
        tabMenu.getArmorSection().setSlots(armorSlots);
        tabMenu.getItemsSection().setSlots(itemsSlots);
        tabMenu.getActionsSection().setSlots(actionsSlots);

        tabMenu.pack();

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            playerController.toggleMenu();
            tabMenu.setVisible(playerController.isMenuOpen());
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        shape.dispose();
        stage.dispose();
        tabMenu.clear();
    }
}
