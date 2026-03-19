package me.arcademadness.omni_dungeon.environment.world;

import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.entities.Entity;

import java.util.HashMap;
import java.util.Map;

public class FlowFieldManager {

    private final Map<Entity, FlowField> fields = new HashMap<>();
    private final Map<Entity, int[]> lastGoal = new HashMap<>();

    public FlowField getField(Floor map, Entity target) {
        FlowField field = fields.get(target);
        Location loc = target.getLocation();
        int goalX = loc.getTileX();
        int goalY = loc.getTileY();

        int[] last = lastGoal.get(target);

        if (field == null) {
            // First-time generation
            field = FlowFieldGenerator.generate(map, goalX, goalY);
            fields.put(target, field);
            lastGoal.put(target, new int[]{goalX, goalY});
        } else if (last[0] != goalX || last[1] != goalY) {
            // Target moved → partial update
            FlowFieldGenerator.updatePartial(field, map, last[0], last[1], goalX, goalY);
            lastGoal.put(target, new int[]{goalX, goalY});
        }

        return field;
    }

    public void clearForTarget(Entity target) {
        fields.remove(target);
        lastGoal.remove(target);
    }

    public void clear() {
        fields.clear();
        lastGoal.clear();
    }
}
