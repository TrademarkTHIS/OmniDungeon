package me.arcademadness.omni_dungeon.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.modifiers.SprintModifier;

public class Sword extends BaseItem{

    public Sword() {
        super(
            "Sword"
            , "This is a sword."
            , new TextureRegion(new Texture("assets/sword.png"))
        );
        addAction(new MoveAction(10, 10));
        SprintModifier sm = new SprintModifier(10);
        sm.setEnabled(true);
        addModifier(sm);
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public ItemType getType() {
        return null;
    }
}
