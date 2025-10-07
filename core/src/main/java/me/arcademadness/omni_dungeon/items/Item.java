package me.arcademadness.omni_dungeon.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.arcademadness.omni_dungeon.actions.Action;
import me.arcademadness.omni_dungeon.modifiers.AttributeModifier;

import java.util.List;

public interface Item {
    String getName();
    String getDescription();

    TextureRegion getTexture();
    int getMaxStackSize();

    List<AttributeModifier<?>> getModifiers();
    List<Action> getActions();

    ItemType getType();
}
