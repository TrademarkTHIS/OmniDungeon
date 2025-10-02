package me.arcademadness.omni_dungeon;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("OmniDungeon");
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new GridGame(), config);
    }
}
