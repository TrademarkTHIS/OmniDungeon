package me.arcademadness.omni_dungeon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SlotWidget extends Button {

    public enum State { FILLED, EMPTY, LOCKED }

    private State state;
    private final Drawable filledDrawable;
    private final Drawable emptyDrawable;
    private final Drawable lockedDrawable;
    private final Image itemImage;

    public SlotWidget(Skin skin, State initialState, int slotSize) {
        super(makeStyle(skin, initialState, slotSize));
        this.state = initialState;

        emptyDrawable  = makeColorDrawable(new Color(1, 1, 1, 0f), slotSize);
        filledDrawable = makeColorDrawable(new Color(1, 1, 1, 0.15f), slotSize);
        lockedDrawable = makeColorDrawable(new Color(0.2f, 0.2f, 0.2f, 0.6f), slotSize);

        itemImage = new Image();
        itemImage.setSize(slotSize, slotSize);
        add(itemImage).expand().center();
    }

    public void setItemTexture(TextureRegion texture) {
        if (texture == null) {
            itemImage.setDrawable(null);
            setState(State.EMPTY);
        } else {
            itemImage.setDrawable(new TextureRegionDrawable(texture));
            setState(State.FILLED);
        }
    }

    public void clearItem() {
        itemImage.setDrawable(null);
        setState(State.EMPTY);
    }

    public void setState(State state) {
        this.state = state;
        getStyle().up = getDrawableFor(state);
    }

    private static ButtonStyle makeStyle(Skin skin, State state, int slotSize) {
        ButtonStyle style = new ButtonStyle();
        style.up = getDrawableForStatic(state, slotSize);
        return style;
    }

    private Drawable getDrawableFor(State state) {
        switch (state) {
            case FILLED: return filledDrawable;
            case EMPTY:  return emptyDrawable;
            case LOCKED: return lockedDrawable;
        }
        return emptyDrawable;
    }

    private static Drawable getDrawableForStatic(State state, int slotSize) {
        Color color;
        switch (state) {
            case FILLED: color = new Color(1, 1, 1, 0.15f); break;
            case EMPTY:  color = new Color(1, 1, 1, 0.10f); break;
            case LOCKED: color = new Color(0.2f, 0.2f, 0.2f, 0.6f); break;
            default: color = new Color(1, 1, 1, 0f);
        }
        return makeColorDrawable(color, slotSize);
    }

    private static Drawable makeColorDrawable(Color color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
