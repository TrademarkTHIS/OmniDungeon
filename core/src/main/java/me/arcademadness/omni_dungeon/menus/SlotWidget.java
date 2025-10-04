package me.arcademadness.omni_dungeon.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SlotWidget extends Button {

    public enum State { FILLED, EMPTY, LOCKED }

    private State state;
    private final Drawable filledDrawable;
    private final Drawable emptyDrawable;
    private final Drawable lockedDrawable;

    public SlotWidget(Skin skin, State initialState, int slotSize) {
        super(makeStyle(skin, initialState, slotSize));
        this.state = initialState;

        filledDrawable = makeColorDrawable(Color.DARK_GRAY, slotSize);
        emptyDrawable  = makeColorDrawable(Color.LIGHT_GRAY, slotSize);
        lockedDrawable = makeColorDrawable(Color.DARK_GRAY.cpy().mul(0.5f), slotSize);
    }

    public void setState(State state) {
        this.state = state;
        getStyle().up = getDrawableFor(state);
    }

    private static ButtonStyle makeStyle(Skin skin, State state, int slotSize) {
        ButtonStyle style = new ButtonStyle();
        style.up = getDrawableFor(state, slotSize);
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

    private static Drawable getDrawableFor(State state, int slotSize) {
        Color color;
        switch (state) {
            case FILLED: color = Color.DARK_GRAY; break;
            case EMPTY: color = Color.LIGHT_GRAY; break;
            case LOCKED: color = Color.DARK_GRAY.cpy().mul(0.5f); break;
            default: color = Color.LIGHT_GRAY;
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
