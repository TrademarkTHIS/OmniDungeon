package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.visuals.Visual;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a visual and physical component of an {@link Entity}.
 * <p>
 * Each EntityPart may have a parent (for hierarchical transforms) and children
 * (for multi-part entities such as humanoids or machines).
 * If no parent is defined, the part’s position is relative to the Entity’s {@link me.arcademadness.omni_dungeon.components.Location}.
 * </p>
 */
public class EntityPart implements Iterable<EntityPart> {

    private final Entity owner;
    private final Vector2 offset = new Vector2(0, 0);

    private @Nullable Visual visual;
    private @Nullable Rectangle collider;

    private @Nullable EntityPart parent;
    private final List<EntityPart> children = new ArrayList<>();

    private float rotation = 0f;

    public EntityPart(Entity owner) {
        this.owner = owner;
    }

    public EntityPart(Entity owner, @Nullable Visual visual) {
        this.owner = owner;
        this.visual = visual;
    }

    public EntityPart(Entity owner, @Nullable Visual visual, Rectangle collider) {
        this.owner = owner;
        this.visual = visual;
        this.collider = new Rectangle(collider);
    }

    public void setParent(@Nullable EntityPart parent) {
        this.parent = parent;
    }

    public void addChild(EntityPart child) {
        if (child == this) throw new IllegalArgumentException("EntityPart cannot be its own child.");
        if (!children.contains(child)) {
            children.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(EntityPart child) {
        if (children.remove(child)) {
            child.setParent(null);
        }
    }

    public List<EntityPart> getChildren() {
        return children;
    }

    public void setOffset(float offsetX, float offsetY) {
        this.offset.set(offsetX, offsetY);
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public float getWorldRotation() {
        return rotation + (parent != null ? parent.getWorldRotation() : 0f);
    }

    public float getWorldX() {
        float baseX;
        if (parent != null) {
            float radians = (float) Math.toRadians(parent.getWorldRotation());
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);
            float rotatedX = offset.x * cos - offset.y * sin;
            baseX = parent.getWorldX() + rotatedX;
        } else {
            baseX = owner.getLocation().x + offset.x;
        }

        return baseX * EnvironmentConfig.get().getTileSize();
    }

    public float getWorldY() {
        float baseY;
        if (parent != null) {
            float radians = (float) Math.toRadians(parent.getWorldRotation());
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);
            float rotatedY = offset.x * sin + offset.y * cos;
            baseY = parent.getWorldY() + rotatedY;
        } else {
            baseY = owner.getLocation().y + offset.y;
        }

        return baseY * EnvironmentConfig.get().getTileSize();
    }

    public @Nullable Visual getVisual() {
        return visual;
    }

    public void setVisual(@Nullable Visual visual) {
        this.visual = visual;
    }

    public @Nullable Rectangle getCollider() {
        if (collider == null) return null;
        return new Rectangle(getWorldX(), getWorldY(), collider.width, collider.height);
    }

    public void setCollider(@Nullable Rectangle collider) {
        this.collider = collider != null ? new Rectangle(collider) : null;
    }

    public Entity getOwner() {
        return owner;
    }

    public @Nullable EntityPart getParent() {
        return parent;
    }

    /**
     * Recursively applies an action to this part and all descendants.
     */
    public void forEachPart(Consumer<EntityPart> action) {
        action.accept(this);
        for (EntityPart child : children) {
            child.forEachPart(action);
        }
    }

    @NotNull
    @Override
    public Iterator<EntityPart> iterator() {
        return children.iterator();
    }
}
