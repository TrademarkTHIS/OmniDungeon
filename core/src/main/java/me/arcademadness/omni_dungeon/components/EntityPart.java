package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.TileMap;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.visuals.Visual;
import org.jetbrains.annotations.Nullable;

public class EntityPart {
    private Visual visual = null;
    private Rectangle collider = null;
    private final Entity owner;
    private EntityPart parent = null;

    private final Vector2 offset = new Vector2(0, 0);
    private float rotation = 0;

    public EntityPart(Entity owner) {
        this.owner = owner;
    }

    public EntityPart(Entity owner, Visual visual) {
        this.owner = owner;
        this.visual = visual;
    }

    public EntityPart(Entity owner, Visual visual, Rectangle collider) {
        this.owner = owner;
        this.visual = visual;
        this.collider = new Rectangle(collider);
    }

    public void setParent(EntityPart parent) {
        this.parent = parent;
    }

    public void setOffset(float offsetX, float offsetY) {
        this.offset.set(offsetX, offsetY);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Entity getOwner() {return owner;}

    @Nullable
    public Visual getVisual() {
        return visual;
    }

    public float getWorldX() {
        float x = offset.x;
        if (parent != null) {
            float radians = (float) Math.toRadians(parent.getWorldRotation());
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);
            float rotatedX = offset.x * cos - offset.y * sin;
            x = parent.getWorldX() + rotatedX;
        } else {
            x += owner.getLocation().x;
        }
        return x;
    }

    public float getWorldY() {
        float y = offset.y;
        if (parent != null) {
            float radians = (float) Math.toRadians(parent.getWorldRotation());
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);
            float rotatedY = offset.x * sin + offset.y * cos;
            y = parent.getWorldY() + rotatedY;
        } else {
            y += owner.getLocation().y;
        }
        return y;
    }


    public float getWorldRotation() {
        return rotation + (parent != null ? parent.getWorldRotation() : 0);
    }

    @Nullable
    public Rectangle getCollider() {
        if (collider == null) return null;

        return new Rectangle(getWorldX(), getWorldY(), collider.width, collider.height);
    }

    public Vector2 getOffset() {
        return offset;
    }
}
