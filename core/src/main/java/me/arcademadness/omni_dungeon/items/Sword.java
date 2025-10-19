package me.arcademadness.omni_dungeon.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.modifiers.SprintModifier;
import me.arcademadness.omni_dungeon.util.AssetLoader;

public class Sword extends BaseItem{

    public Sword() {
        super(
            "Sword"
            , "This is a sword."
            , new TextureRegion(AssetLoader.loadTexture("assets/sword.png"))
        );
        addAction(new MoveAction(new Vector2(10,10)));
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
