package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import me.arcademadness.omni_dungeon.modifiers.SprintModifier;

public class PlayerController extends AbstractController {
    private final SprintModifier sprintModifier = new SprintModifier(5);
    private boolean menuOpen = false;


    public void toggleMenu() {
        menuOpen = !menuOpen;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    @Override
    public ControlIntent getIntent() {
        Entity entity = getEntity();
        ControlIntent intent = new ControlIntent();
        if (menuOpen) return intent;

        // handle movement
        float dx = 0, dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1;
        if (dx != 0 && dy != 0) { dx *= 0.707f; dy *= 0.707f; }

        handleSprint(entity);

        if (dx != 0 || dy != 0)
            intent.addAction(new MoveAction(dx, dy));

        return intent;
    }

    private void handleSprint(Entity entity) {
        boolean shiftDown = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        AttributeModifier<Float> existing = entity.getMaxSpeed().getFirstModifier(SprintModifier.class);

        if (existing == null) {
            entity.getMaxSpeed().addModifier(sprintModifier);
            entity.getAcceleration().addModifier(sprintModifier);
        } else if (existing != sprintModifier) {
            entity.getMaxSpeed().removeModifier(existing);
            entity.getAcceleration().removeModifier(existing);
            entity.getMaxSpeed().addModifier(sprintModifier);
            entity.getAcceleration().addModifier(sprintModifier);
        }

        sprintModifier.setEnabled(shiftDown);
    }

}

