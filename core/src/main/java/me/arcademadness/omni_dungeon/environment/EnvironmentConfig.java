package me.arcademadness.omni_dungeon.environment;

public final class EnvironmentConfig {
    private static EnvironmentConfig instance;

    private final int tileSize;

    private EnvironmentConfig(int tileSize) {
        this.tileSize = tileSize;
    }

    public static EnvironmentConfig initialize(int tileSize) {
        if (instance != null) {
            throw new IllegalStateException("EnvironmentConfig has already been initialized");
        }
        instance = new EnvironmentConfig(tileSize);
        return instance;
    }

    public static EnvironmentConfig get() {
        if (instance == null) {
            return initialize(32); // Default to 32
        }
        return instance;
    }

    public int getTileSize() {
        return tileSize;
    }
}

