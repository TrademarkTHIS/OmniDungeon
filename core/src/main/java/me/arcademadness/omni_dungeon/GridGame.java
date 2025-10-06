package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.arcademadness.omni_dungeon.controllers.PlayerController;
import me.arcademadness.omni_dungeon.controllers.MobController;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;
import me.arcademadness.omni_dungeon.menus.SlotWidget;
import me.arcademadness.omni_dungeon.menus.TabMenu;
import me.arcademadness.omni_dungeon.render.EntityRenderer;
import me.arcademadness.omni_dungeon.ui.MenuManager;
import me.arcademadness.omni_dungeon.ui.MenuScreen;
import me.arcademadness.omni_dungeon.render.FogRenderer;
import me.arcademadness.omni_dungeon.render.WorldRenderer;
import me.arcademadness.omni_dungeon.visuals.ShapeVisual;

public class GridGame extends ApplicationAdapter {
    private ShapeRenderer shape;
    private World world;
    private PlayerEntity player;
    private PlayerController playerController;

    // Rendering/camera
    private OrthographicCamera worldCamera;
    private Viewport worldViewport;

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private Stage uiStage;

    // Renderers
    private WorldRenderer worldRenderer;
    private FogRenderer fogRenderer;
    private EntityRenderer entityRenderer;

    // UI & menus
    private MenuManager menuManager;
    private TabMenu tabMenu;

    // Zoom
    private float cameraZoom = 1f;
    private float targetZoom = 1f;
    private final float MIN_ZOOM = 0.5f;
    private final float MAX_ZOOM = 5f;
    private final float ZOOM_SPEED = 0.2f;

    @Override
    public void create() {
        shape = new ShapeRenderer();

        // World setup
        TileMap map = new TileMap(40, 30);
        world = new World(map);

        player = new PlayerEntity(map.width / 2, map.height / 2);
        player.setVisual(new ShapeVisual(Color.CYAN));
        playerController = new PlayerController();
        player.setController(playerController);
        world.addEntity(player);

        // Example mob
        MobEntity redMob = new MobEntity(3, 3);
        redMob.setVisual(new ShapeVisual(Color.RED));
        redMob.setController(new MobController());
        world.addEntity(redMob);

        // Cameras & viewports
        worldCamera = new OrthographicCamera();

        worldViewport = new ExtendViewport(
            map.width * TileMap.TILE_SIZE,
            map.height * TileMap.TILE_SIZE,
            worldCamera
        );

        uiCamera = new OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);

        uiStage = new Stage(uiViewport);

        // Renderers
        fogRenderer = new FogRenderer(world, shape, 12);
        worldRenderer = new WorldRenderer(world, shape, fogRenderer);
        entityRenderer = new EntityRenderer(world, fogRenderer, shape);

        // UI & menus
        menuManager = new MenuManager(uiStage);
        Skin skin = new Skin(Gdx.files.absolute("assets/uiskin.json"));
        tabMenu = new TabMenu(skin);
        tabMenu.setColor(1f, 1f, 1f, 0.95f);
        populateTabMenu(tabMenu, skin);

        Table root = new Table();
        root.setFillParent(true);
        root.add(tabMenu).center();
        uiStage.addActor(root);

        menuManager.register("tab", (MenuScreen) tabMenu);
        menuManager.hideAll();

        // Input handling
        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(uiStage);
        mux.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (!playerController.isMenuOpen()) {
                    targetZoom += amountY * ZOOM_SPEED;
                    targetZoom = MathUtils.clamp(targetZoom, MIN_ZOOM, MAX_ZOOM);
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(mux);

        centerCameraOnPlayer();
    }

    private void centerCameraOnPlayer() {
        float px = player.getLocation().getX() * TileMap.TILE_SIZE + TileMap.TILE_SIZE / 2f;
        float py = player.getLocation().getY() * TileMap.TILE_SIZE + TileMap.TILE_SIZE / 2f;
        worldCamera.position.set(px, py, 0);
        worldCamera.update();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        world.tick(delta);

        // Smooth camera zoom
        cameraZoom += (targetZoom - cameraZoom) * 0.15f;
        worldCamera.zoom = cameraZoom;

        // Smooth camera follow
        float px = player.getLocation().getX() * TileMap.TILE_SIZE + TileMap.TILE_SIZE / 2f;
        float py = player.getLocation().getY() * TileMap.TILE_SIZE + TileMap.TILE_SIZE / 2f;
        worldCamera.position.lerp(new Vector3(px, py, 0), 0.2f);
        worldCamera.update();

        // Clear screen
        ScreenUtils.clear(Color.BLACK);


        worldViewport.apply();
        worldRenderer.render(worldCamera);
        entityRenderer.render(worldCamera, player);
        fogRenderer.render(worldCamera, player); // drawn last (overlay)


        // Render UI
        uiViewport.apply();
        uiCamera.position.set(uiViewport.getWorldWidth() / 2f, uiViewport.getWorldHeight() / 2f, 0);
        uiCamera.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            playerController.toggleMenu();
            if (playerController.isMenuOpen()) menuManager.show("tab");
            else menuManager.hideAll();
        }

        uiStage.act(delta);
        uiStage.draw();
    }

    private void populateTabMenu(TabMenu tabMenu, Skin skin) {
        java.util.function.Function<Integer, com.badlogic.gdx.utils.Array<SlotWidget>> makeSlots = (count) -> {
            com.badlogic.gdx.utils.Array<SlotWidget> arr = new com.badlogic.gdx.utils.Array<>();
            for (int i = 0; i < count; i++) {
                arr.add(new SlotWidget(skin, SlotWidget.State.EMPTY, 48));
            }
            return arr;
        };

        com.badlogic.gdx.utils.Array<SlotWidget> inventorySlots = makeSlots.apply(500);

        com.badlogic.gdx.utils.Array<SlotWidget> armorSlots = new com.badlogic.gdx.utils.Array<>();
        for (int i = 0; i < 18; i++) {
            SlotWidget.State state = i < 6 ? SlotWidget.State.EMPTY
                : i < 12 ? SlotWidget.State.FILLED
                : SlotWidget.State.LOCKED;
            armorSlots.add(new SlotWidget(skin, state, 48));
        }

        com.badlogic.gdx.utils.Array<SlotWidget> itemsSlots = new com.badlogic.gdx.utils.Array<>();
        for (int i = 0; i < 27; i++) {
            itemsSlots.add(new SlotWidget(skin, SlotWidget.State.FILLED, 48));
        }

        com.badlogic.gdx.utils.Array<SlotWidget> actionsSlots = makeSlots.apply(18);

        tabMenu.getInventorySection().setSlots(inventorySlots);
        tabMenu.getArmorSection().setSlots(armorSlots);
        tabMenu.getItemsSection().setSlots(itemsSlots);
        tabMenu.getActionsSection().setSlots(actionsSlots);

        tabMenu.pad(10).defaults().space(5);
        tabMenu.pack();
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shape.dispose();
        uiStage.dispose();
    }
}
