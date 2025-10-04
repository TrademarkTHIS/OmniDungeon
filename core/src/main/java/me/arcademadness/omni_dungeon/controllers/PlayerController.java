package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.movement.MovementIntent;
import me.arcademadness.omni_dungeon.modifiers.SprintModifier;

public class PlayerController implements Controller {
    private final SprintModifier sprintModifier = new SprintModifier();
    private boolean sprintApplied = false;

    @Override
    public MovementIntent getIntent(Entity entity) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1;

        // Normalize diagonal
        if (dx != 0 && dy != 0) {
            dx *= 0.707f;
            dy *= 0.707f;
        }

        handleSprint(entity);

        return new MovementIntent(dx, dy);
    }

    private void handleSprint(Entity entity) {
        boolean shiftDown = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        if (shiftDown && !sprintApplied) {
            entity.getMaxSpeed().addModifier(sprintModifier);
            sprintApplied = true;
        } else if (!shiftDown && sprintApplied) {
            entity.getMaxSpeed().removeModifier(sprintModifier);
            sprintApplied = false;
        }
    }
}
