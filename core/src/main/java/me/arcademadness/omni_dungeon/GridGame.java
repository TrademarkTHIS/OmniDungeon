package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import me.arcademadness.omni_dungeon.controllers.MobController;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;
import me.arcademadness.omni_dungeon.controllers.PlayerController;

public class GridGame extends ApplicationAdapter {
    ShapeRenderer shape;
    World world;
    PlayerEntity player;
    PlayerController playerController;

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
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        for (Entity e : world.getEntities()) {
            e.getController().update(e, world, delta);
        }

        ScreenUtils.clear(Color.BLACK);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = 0; x < world.getMap().width; x++) {
            for (int y = 0; y < world.getMap().height; y++) {
                Tile t = world.getMap().tiles[x][y];
                shape.setColor(t.walkable ? Color.FOREST : Color.DARK_GRAY);
                shape.rect(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
            }
        }

        for (Entity e : world.getEntities()) {
            e.render(shape);
        }

        shape.end();
    }
}


