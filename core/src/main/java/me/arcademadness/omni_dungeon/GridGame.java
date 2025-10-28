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
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.controllers.*;
import me.arcademadness.omni_dungeon.entities.*;
import me.arcademadness.omni_dungeon.environment.Environment;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.render.*;
import me.arcademadness.omni_dungeon.ui.InventoryMenu;
import me.arcademadness.omni_dungeon.ui.MenuManager;
import me.arcademadness.omni_dungeon.environment.world.TileMap;

public class GridGame extends ApplicationAdapter {

    // Rendering
    private ShapeRenderer shape;
    private OrthographicCamera worldCamera, uiCamera;
    private Viewport worldViewport, uiViewport;
    private Stage uiStage;

    // World
    private Environment environment;
    private PlayerEntity player;
    private PlayerController playerController;

    // Renderers
    private LayerManager layerManager;
    private WorldRenderer worldRenderer;
    private EntityRenderer entityRenderer;
    private FogRenderer fogRenderer;
    private WeaponSpreadRenderer weaponSpreadRenderer;

    // UI
    private MenuManager menuManager;
    private InventoryMenu inventoryMenu;

    // Camera Zoom
    private float cameraZoom = 1f;
    private float targetZoom = 1f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 10f;
    private static final float ZOOM_SPEED = 0.4f;
    private static final float ZOOM_LERP = 0.15f;
    private static final float CAMERA_LERP = 0.2f;

    @Override
    public void create() {
        shape = new ShapeRenderer();

        setupWorld();
        setupCameras();
        setupRenderers();

        setupUI();
        setupInput();

        centerCameraOnPlayer();
    }

    private void setupWorld() {
        EnvironmentConfig.initialize(32);
        TileMap map = new TileMap(100, 100);
        environment = new Environment(map);

        // Player
        player = new PlayerEntity();
        playerController = new PlayerController();
        player.setController(playerController);
        environment.spawn(player, new Location(map.width / 2f, map.height / 2f));

        int beesToGrid = 15;
        float spacing = 0.3f;

        for (int i = 0; i < beesToGrid; i++) {
            for (int j = 0; j < beesToGrid; j++) {
                float xOffset = (i - beesToGrid / 2f) * spacing;
                float yOffset = (j - beesToGrid / 2f) * spacing;
                environment.spawn(new BeeEntity(), new Location((map.width / 2f) + 25 + xOffset, (map.height / 2f) + 25 + yOffset));
            }
        }

        RedMobEntity redMob = new RedMobEntity();
        environment.spawn(redMob, new Location(3,3));
    }

    private void setupCameras() {
        worldCamera = new OrthographicCamera();

        float virtualWidth = 16 * environment.getMap().getTileSize();
        float virtualHeight = 16 * environment.getMap().getTileSize();

        worldViewport = new ExtendViewport(virtualWidth, virtualHeight, worldCamera);

        uiCamera = new OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);
        uiStage = new Stage(uiViewport);
    }


    private void setupRenderers() {
        fogRenderer = new FogRenderer(environment, player, shape, 32);
        worldRenderer = new WorldRenderer(environment, shape, fogRenderer);
        entityRenderer = new EntityRenderer(environment, player, fogRenderer, shape);
        weaponSpreadRenderer = new WeaponSpreadRenderer(player, shape);


        layerManager = new LayerManager();
        layerManager.addLayer(worldRenderer);
        layerManager.addLayer(entityRenderer);
        layerManager.addLayer(fogRenderer);
        layerManager.addLayer(weaponSpreadRenderer);
    }

    private void setupUI() {
        Skin skin = new Skin(Gdx.files.absolute("assets/uiskin.json"));
        inventoryMenu = new InventoryMenu(skin, player);
        inventoryMenu.populate();

        Table root = new Table();
        root.setFillParent(true);
        root.add(inventoryMenu).center();
        uiStage.addActor(root);

        menuManager = new MenuManager(uiStage);
        menuManager.register("inventory", inventoryMenu);
        menuManager.hideAll();
    }

    private void setupInput() {
        InputMultiplexer mux = new InputMultiplexer(uiStage, new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (!playerController.isMenuOpen()) {
                    targetZoom = MathUtils.clamp(targetZoom + amountY * ZOOM_SPEED, MIN_ZOOM, MAX_ZOOM);
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(mux);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        environment.tick(delta);

        updateCamera(delta);
        handleMenuToggle();

        ScreenUtils.clear(Color.BLACK);

        worldViewport.apply();
        layerManager.renderAll(worldCamera);

        uiViewport.apply();
        uiCamera.position.set(uiViewport.getWorldWidth() / 2f, uiViewport.getWorldHeight() / 2f, 0);
        uiCamera.update();

        uiStage.act(delta);
        uiStage.draw();
    }

    private void updateCamera(float delta) {
        TileMap map = environment.getMap();
        cameraZoom += (targetZoom - cameraZoom) * ZOOM_LERP;
        worldCamera.zoom = cameraZoom;

        float px = player.getLocation().getX() * (map.getTileSize()*2) / 2f;
        float py = player.getLocation().getY() * (map.getTileSize()*2) / 2f;
        worldCamera.position.lerp(new Vector3(px, py, 0), CAMERA_LERP);
        worldCamera.update();
    }

    private void centerCameraOnPlayer() {
        TileMap map = environment.getMap();
        float px = player.getLocation().getX() * (map.getTileSize()*2) / 2f;
        float py = player.getLocation().getY() * (map.getTileSize()*2) / 2f;
        worldCamera.position.set(px, py, 0);
        worldCamera.update();
    }

    private void handleMenuToggle() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            playerController.toggleMenu();
            if (playerController.isMenuOpen()) menuManager.show("inventory");
            else menuManager.hideAll();
        }
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiViewport.update(width, height, true);
        inventoryMenu.resizeForScreen(width,height);
    }

    @Override
    public void dispose() {
        shape.dispose();
        uiStage.dispose();
    }
}
