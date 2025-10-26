package me.arcademadness.omni_dungeon.controllers;

import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.actions.MoveAction;
import me.arcademadness.omni_dungeon.components.EntityPart;
import me.arcademadness.omni_dungeon.components.Location;
import me.arcademadness.omni_dungeon.entities.BeeEntity;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentView;
import me.arcademadness.omni_dungeon.environment.world.AStar;
import me.arcademadness.omni_dungeon.environment.world.Tile;
import me.arcademadness.omni_dungeon.environment.world.TileMap;
import me.arcademadness.omni_dungeon.events.EventListener;
import me.arcademadness.omni_dungeon.events.Subscribe;
import me.arcademadness.omni_dungeon.events.entity.BeeStingEvent;
import me.arcademadness.omni_dungeon.events.PartCollisionEvent;

import java.awt.*;
import java.util.List;
import java.util.Optional;


public class BeeController extends AbstractController implements EventListener {
    private static final int SCAN_RADIUS = 50;

    @Subscribe
    public void onCollision(PartCollisionEvent event) {
        if (event.getColliderPart().getOwner() != entity) return;
        if (entity instanceof BeeEntity bee) {
            Entity target = event.getCollideePart().getOwner();
            if (target instanceof BeeEntity) return;

            BeeStingEvent beeSting = new BeeStingEvent(target, bee, 1);
            if (bee.getEnvironment() != null) {
                bee.getEnvironment().getEventBus().post(beeSting);
            }
        }
    }

    @Override
    public Optional<ControlIntent> getIntent() {
        Entity entity = getEntity();
        EnvironmentView env = entity.getEnvironment();
        TileMap map = env.getMap();

        Location loc = entity.getLocation();

        Entity closest = findClosestEntity(map, entity, SCAN_RADIUS);
        if (closest == null) return Optional.empty();

        List<Point> path = AStar.findPath(
            map,
            loc.getTileX(), loc.getTileY(),
            closest.getLocation().getTileX(), closest.getLocation().getTileY()
        );


        if (path.isEmpty()) return Optional.empty();

        Point next = path.get(0);
        Vector2 dir = new Vector2(next.x - loc.getTileX(), next.y - loc.getTileY()).nor();

        ControlIntent intent = new ControlIntent();
        intent.addAction(new MoveAction(dir));
        return Optional.of(intent);
    }

    private Entity findClosestEntity(TileMap map, Entity self, int radius) {
        Location loc = self.getLocation();
        Entity closest = null;
        float bestDistSq = Float.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = loc.getTileX() + dx;
                int y = loc.getTileY() + dy;
                if (x < 0 || y < 0 || x >= map.width || y >= map.height) continue;

                Tile tile = map.tiles[x][y];
                for (EntityPart part : tile.parts) {
                    Entity e = part.getOwner();
                    if (e == self) continue;
                    if (e instanceof BeeEntity) continue;

                    Vector2 pos = e.getLocation();
                    float distSq = loc.dst2(pos);
                    if (distSq < bestDistSq) {
                        bestDistSq = distSq;
                        closest = e;
                    }
                }
            }
        }
        return closest;
    }
}

