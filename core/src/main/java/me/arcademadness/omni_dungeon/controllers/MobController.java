package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.Random;

public class MobController extends AbstractController {
    private static final float CHANGE_DIR_TIME = 2f;

    private final Entity entity;

    private final Random random = new Random();
    private float dirX = 0;
    private float dirY = 0;
    private float timer = 0;

    public MobController(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ControlIntent getIntent() {
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;

        if (timer >= CHANGE_DIR_TIME) {
            timer = 0;
            pickNewDirection();
        }

        ControlIntent intent = new ControlIntent();

        if (dirX != 0 || dirY != 0) {
            intent.addAction(new MoveAction(dirX, dirY));
        }

        return intent;
    }

    private void pickNewDirection() {
        int dir = random.nextInt(5);
        switch (dir) {
            case 0:
                dirX = 0;
                dirY = 1;
                break;
            case 1:
                dirX = 0;
                dirY = -1;
                break;
            case 2:
                dirX = -1;
                dirY = 0;
                break;
            case 3:
                dirX = 1;
                dirY = 0;
                break;
            default:
                dirX = 0;
                dirY = 0;
                break;
        }

        if (dirX != 0 && dirY != 0) {
            dirX *= 0.707f;
            dirY *= 0.707f;
        }
    }

}
