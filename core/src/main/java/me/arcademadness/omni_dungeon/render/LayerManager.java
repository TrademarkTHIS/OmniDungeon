package me.arcademadness.omni_dungeon.render;

import com.badlogic.gdx.graphics.Camera;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayerManager {
    private final List<RenderLayer> layers = new ArrayList<>();

    public void addLayer(RenderLayer layer) {
        layers.add(layer);
    }

    public void insertLayer(int index, RenderLayer layer) {
        layers.add(index, layer);
    }

    public void removeLayer(RenderLayer layer) {
        layers.remove(layer);
    }

    public void moveLayer(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= layers.size() || toIndex >= layers.size()) return;
        Collections.swap(layers, fromIndex, toIndex);
    }

    public void renderAll(Camera camera) {
        for (RenderLayer layer : layers) {
            layer.render(camera);
        }
    }

    public List<RenderLayer> getLayers() {
        return Collections.unmodifiableList(layers);
    }
}
