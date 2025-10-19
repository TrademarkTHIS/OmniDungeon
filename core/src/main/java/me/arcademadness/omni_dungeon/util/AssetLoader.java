package me.arcademadness.omni_dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;

public class AssetLoader {

    private static final String DEFAULT_ASSET_PATH = "default_assets/";

    /**
     * Loads a texture from the given path, falling back to a given default texture if missing.
     * @param path The path to the texture (e.g. "assets/sword.png")
     * @param defaultName The fallback filename inside default_assets/ (e.g. "default_item.png")
     */
    public static Texture loadTexture(String path, String defaultName) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                Gdx.app.error("AssetLoader", "Missing texture: " + path + " — using default instead.");
                return new Texture(Gdx.files.internal(DEFAULT_ASSET_PATH + defaultName));
            }

            return new Texture(Gdx.files.internal(path));
        } catch (GdxRuntimeException e) {
            Gdx.app.error("AssetLoader", "Error loading texture " + path + ", using default.", e);
            return new Texture(Gdx.files.internal(DEFAULT_ASSET_PATH + defaultName));
        }
    }

    /**
     * Loads a texture from the given path, falling back to specifically default.png if missing.
     * @param path The path to the texture (e.g. "assets/sword.png")
     */
    public static Texture loadTexture(String path) {
        return AssetLoader.loadTexture(path, "default.png");
    }
}
