package me.arcademadness.omni_dungeon.environment;

public final class EnvironmentConfig {
    private static EnvironmentConfig instance;

    private final int tileSize;

    private EnvironmentConfig(int tileSize) {
        this.tileSize = tileSize;
    }

    public static void initialize(int tileSize) {
        if (instance != null) {
            throw new IllegalStateException("EnvironmentConfig has already been initialized");
        }
        instance = new EnvironmentConfig(tileSize);
    }

    public static EnvironmentConfig get() {
        if (instance == null) {
            throw new IllegalStateException("EnvironmentConfig not initialized");
        }
        return instance;
    }

    public int getTileSize() {
        return tileSize;
    }
}

