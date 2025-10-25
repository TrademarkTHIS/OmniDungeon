package me.arcademadness.omni_dungeon.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class SectionWidget extends Table {
    protected final Table grid;
    protected final int columns;
    protected final int slotSize;
    protected final ScrollPane scrollPane;
    protected float visibleHeight = -1f;
    protected float slotPadding = 4f;

    public SectionWidget(int columns, int slotSize, Color backgroundColor, Skin skin) {
        this.columns = columns;
        this.slotSize = slotSize;

        setBackground(makeColorDrawable(backgroundColor));

        grid = new Table();
        grid.defaults().size(slotSize).pad(slotPadding);

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = null;

        scrollPane = new ScrollPane(grid, style);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsVisible(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setForceScroll(false, true);

        // Handle mouse scroll
        scrollPane.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                scrollPane.setScrollY(scrollPane.getScrollY() + amountY * 40f);
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                scrollPane.getStage().setScrollFocus(scrollPane);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (scrollPane.getStage().getScrollFocus() == scrollPane)
                    scrollPane.getStage().setScrollFocus(null);
            }
        });

        add(scrollPane).growX().height(computeVisibleHeight());
    }

    public void setSlots(Array<SlotWidget> slots) {
        grid.clearChildren();

        for (int i = 0; i < slots.size; i++) {
            grid.add(slots.get(i));
            if ((i + 1) % columns == 0)
                grid.row();
        }

        grid.invalidateHierarchy();
        scrollPane.layout();
    }

    public void setVisibleRows(int visibleRows) {
        float rowHeight = slotSize + slotPadding * 2;
        this.visibleHeight = rowHeight * visibleRows;

        getCell(scrollPane).height(visibleHeight);
        invalidateHierarchy();
        scrollPane.layout();
    }

    private float computeVisibleHeight() {
        if (visibleHeight > 0) return visibleHeight;
        int totalRows = (int)Math.ceil((float)grid.getChildren().size / columns);
        return totalRows * (slotSize + slotPadding * 2);
    }

    private Drawable makeColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
