package me.arcademadness.omni_dungeon.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItem implements Item {

    protected final String name;
    protected final String description;
    protected final TextureRegion icon;
    protected int maxStackSize = 1;

    private final List<AttributeModifier<?>> modifiers = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();


    protected BaseItem(String name, String description, TextureRegion icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public TextureRegion getTexture() {
        return icon;
    }

    @Override
    public List<AttributeModifier<?>> getModifiers() { return new ArrayList<>(modifiers); }

    @Override
    public List<Action> getActions() { return new ArrayList<>(actions); }

    protected void addModifier(AttributeModifier<?> modifier) {
        modifiers.add(modifier);
    }

    protected void addAction(Action action) {
        actions.add(action);
    }
}
