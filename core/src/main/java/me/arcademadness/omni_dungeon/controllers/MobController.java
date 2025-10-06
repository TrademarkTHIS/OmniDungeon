package me.arcademadness.omni_dungeon.controllers;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import me.arcademadness.omni_dungeon.entities.Entity;

public class MobController implements Controller {
    private static final float SPEED = 2f;
    private static final float CHANGE_DIR_TIME = 2f;

    private final Random random = new Random();
    private float dirX = 0;
    private float dirY = 0;
    private float timer = 0;

    @Override
    public ControlIntent getIntent(Entity entity) {
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;

        if (timer >= CHANGE_DIR_TIME) {
            timer = 0;
            int dir = random.nextInt(5);
            switch (dir) {
                case 0:
                    dirX = 0; dirY = 1;
                    break;
                case 1:
                    dirX = 0; dirY = -1;
                    break;
                case 2:
                    dirX = -1; dirY = 0;
                    break;
                case 3:
                    dirX = 1; dirY = 0;
                    break;
                case 4:
                    dirX = 0; dirY = 0;
                    break;
            }

        }

        if (dirX != 0 && dirY != 0) {
            dirX *= 0.707f;
            dirY *= 0.707f;
        }

        return new ControlIntent(dirX * SPEED, dirY * SPEED);
    }
}
