package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.Optional;
import java.util.Random;

public class MobController extends AbstractController {
    private static final float CHANGE_DIR_TIME = 2f;

    private final Entity entity;
    private final Random random = new Random();

    private final Vector2 direction = new Vector2();
    private float timer = 0;

    public MobController(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Optional<ControlIntent> getIntent() {
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;

        if (timer >= CHANGE_DIR_TIME) {
            timer = 0;
            pickNewDirection();
        }

        if (direction.isZero()) {
            return Optional.empty();
        }

        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(direction.cpy()));
        return Optional.of(intent);
    }

    private void pickNewDirection() {
        int dir = random.nextInt(5);

        switch (dir) {
            case 0: direction.set(0, 1);  break;
            case 1: direction.set(0, -1); break;
            case 2: direction.set(-1, 0); break;
            case 3: direction.set(1, 0);  break;
            default: direction.set(0, 0); break;
        }

        if (!direction.isZero()) {
            direction.nor();
        }
    }
}
