package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;
import me.arcademadness.omni_dungeon.modifiers.SprintModifier;

import java.util.Optional;

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
    public Optional<ControlIntent> getIntent() {
        Entity entity = getEntity();

        if (menuOpen) return Optional.empty();

        Vector2 dir = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dir.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dir.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dir.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dir.x += 1;

        handleSprint(entity);

        // No movement, return early.
        if (dir.x == 0 && dir.y == 0) {
            return Optional.empty();
        }

        // Otherwise create an intent
        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(dir));
        return Optional.of(intent);
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

