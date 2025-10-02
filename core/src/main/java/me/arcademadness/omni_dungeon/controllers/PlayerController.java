package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.movement.MovementIntent;

public class PlayerController implements Controller {
    private static final float SPEED = 3f;

    @Override
    public MovementIntent getIntent(Entity entity) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1;

        if (dx != 0 && dy != 0) {
            dx *= 0.707f;
            dy *= 0.707f;
        }

        return new MovementIntent(dx * SPEED, dy * SPEED);
    }
}
