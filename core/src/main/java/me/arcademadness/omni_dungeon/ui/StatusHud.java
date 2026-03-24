package me.arcademadness.omni_dungeon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import me.arcademadness.omni_dungeon.entities.PlayerEntity;

public class StatusHud extends Table {
    private static final float BAR_HEIGHT = 14f;
    private static final float MIN_BAR_WIDTH_RATIO = 0.12f;
    private static final float MAX_BAR_WIDTH_RATIO = 0.30f;
    private static final float BAR_WIDTH_CURVE_SCALE = 75f;

    private final PlayerEntity player;

    private final StatBar healthBar;
    private final StatBar armorBar;
    // Later:
    // private final StatBar manaBar;
    // private final StatBar staminaBar;

    private static final Texture WHITE_TEXTURE = createWhiteTexture();
    private static final Drawable BAR_BG = tinted(Color.DARK_GRAY);
    private static final Drawable HEALTH_FILL = tinted(Color.RED);
    private static final Drawable ARMOR_FILL = tinted(Color.LIGHT_GRAY);
    private static final Drawable MANA_FILL = tinted(Color.BLUE);
    private static final Drawable STAMINA_FILL = tinted(Color.GREEN);

    public StatusHud(Skin skin, PlayerEntity player) {
        super(skin);
        this.player = player;

        top().left();
        pad(12);
        defaults().left().padBottom(6);

        healthBar = new StatBar(
            skin,
            "Health",
            HEALTH_FILL,
            new StatProvider() {
                @Override
                public double getCurrent() {
                    return StatusHud.this.player.getHealth().getCurrentHealth();
                }

                @Override
                public double getMax() {
                    return StatusHud.this.player.getHealth().getFinalValue();
                }
            }
        );

        armorBar = new StatBar(
            skin,
            "Armor",
            ARMOR_FILL,
            new StatProvider() {
                @Override
                public double getCurrent() {
                    return StatusHud.this.player.getArmor().getFinalValue();
                }

                @Override
                public double getMax() {
                    return StatusHud.this.player.getArmor().getFinalValue();
                }
            }
        );

        add(healthBar).left().row();
        add(armorBar).left().row();

        // Later:
        // manaBar = new StatBar(...);
        // staminaBar = new StatBar(...);
        // add(manaBar).left().row();
        // add(staminaBar).left().row();

        update();
        pack();
    }

    public void update() {
        healthBar.update();
        armorBar.update();
        // Later:
        // manaBar.update();
        // staminaBar.update();
        pack();
    }

    private static float computeBarWidth(float maxValue) {
        float screenWidth = Gdx.graphics.getWidth();

        float minWidth = screenWidth * MIN_BAR_WIDTH_RATIO;
        float maxWidth = screenWidth * MAX_BAR_WIDTH_RATIO;

        float t = 1f - (float) Math.exp(-maxValue / BAR_WIDTH_CURVE_SCALE);
        return MathUtils.lerp(minWidth, maxWidth, t);
    }

    private static Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private static Drawable tinted(Color color) {
        return new TextureRegionDrawable(new TextureRegion(WHITE_TEXTURE)).tint(color);
    }

    private interface StatProvider {
        double getCurrent();
        double getMax();
    }

    private static class StatBar extends Table {
        private final Label nameLabel;
        private final Label valueLabel;
        private final BarActor barActor;
        private final StatProvider provider;

        public StatBar(Skin skin, String name, Drawable fillDrawable, StatProvider provider) {
            super(skin);
            this.provider = provider;

            nameLabel = new Label(name + ":", skin);
            valueLabel = new Label("", skin);
            barActor = new BarActor(BAR_BG, fillDrawable);

            add(nameLabel).width(70).left().padRight(8);
            add(barActor).left().padRight(8);
            add(valueLabel).left();
        }

        public void update() {
            double current = provider.getCurrent();
            double max = provider.getMax();

            double safeMax = Math.max(1.0, max);
            double clampedCurrent = Math.max(0.0, Math.min(current, safeMax));

            float width = computeBarWidth((float) safeMax);

            barActor.setBarSize(width, BAR_HEIGHT);
            barActor.setPercent((float) (clampedCurrent / safeMax));

            String currentText = (clampedCurrent % 1 == 0)
                ? String.valueOf((int) clampedCurrent)
                : String.format("%.1f", clampedCurrent);

            String maxText = (safeMax % 1 == 0)
                ? String.valueOf((int) safeMax)
                : String.format("%.1f", safeMax);

            valueLabel.setText(currentText + " / " + maxText);
        }
    }

    private static class BarActor extends Actor {
        private final Drawable background;
        private final Drawable fill;

        private float percent = 1f;

        public BarActor(Drawable background, Drawable fill) {
            this.background = background;
            this.fill = fill;
            setSize(100f, BAR_HEIGHT);
        }

        public void setBarSize(float width, float height) {
            setSize(width, height);
        }

        public void setPercent(float percent) {
            this.percent = Math.max(0f, Math.min(1f, percent));
        }

        @Override
        public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            background.draw(batch, getX(), getY(), getWidth(), getHeight());
            fill.draw(batch, getX(), getY(), getWidth() * percent, getHeight());

            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
}
