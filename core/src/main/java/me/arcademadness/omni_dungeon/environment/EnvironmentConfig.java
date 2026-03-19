package me.arcademadness.omni_dungeon.environment;

public final class EnvironmentConfig {
    private static EnvironmentConfig instance;

    private final int tileSize;
    private final int chunkSize;

    private EnvironmentConfig(int tileSize, int chunkSize) {
        this.tileSize = tileSize;
        this.chunkSize = chunkSize;
    }

    public static EnvironmentConfig initialize(int tileSize, int chunkSize) {
        if (instance != null) {
            throw new IllegalStateException("EnvironmentConfig has already been initialized");
        }
        instance = new EnvironmentConfig(tileSize, chunkSize);
        return instance;
    }

    public static EnvironmentConfig get() {
        if (instance == null) {
            return initialize(32, 16); // Default to 32
        }
        return instance;
    }

    public int getTileSize() {
        return tileSize;
    }
    public int getChunkSize() {
        return chunkSize;
    }
}

