package me.arcademadness.omni_dungeon.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.environment.EnvironmentConfig;
import me.arcademadness.omni_dungeon.visuals.Visual;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a visual and physical component of an {@link Entity}.
 * <p>
 * Each EntityPart may have a parent (for hierarchical transforms) and children
 * (for multi-part entities such as spiders).
 * If no parent is defined, the part’s position is relative to the Entity’s {@link Location}.
 * </p>
 */
public class EntityPart implements Iterable<EntityPart> {

    private final Entity owner;
    private final Vector2 offset = new Vector2();

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

    public void setLocalPosition(float x, float y) {
        offset.set(x, y);
    }

    public Vector2 getLocalPosition() {
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
        float baseX = parent != null ? getParentRotatedOffsetX() + parent.getWorldX()
            : owner.getLocation().x + offset.x;
        return baseX * EnvironmentConfig.get().getTileSize();
    }

    public float getWorldY() {
        float baseY = parent != null ? getParentRotatedOffsetY() + parent.getWorldY()
            : owner.getLocation().y + offset.y;
        return baseY * EnvironmentConfig.get().getTileSize();
    }

    public float getTileX() {
        return getWorldX() / EnvironmentConfig.get().getTileSize();
    }

    public float getTileY() {
        return getWorldY() / EnvironmentConfig.get().getTileSize();
    }

    private float getParentRotatedOffsetX() {
        float radians = (float) Math.toRadians(parent.getWorldRotation());
        return offset.x * (float) Math.cos(radians) - offset.y * (float) Math.sin(radians);
    }

    private float getParentRotatedOffsetY() {
        float radians = (float) Math.toRadians(parent.getWorldRotation());
        return offset.x * (float) Math.sin(radians) + offset.y * (float) Math.cos(radians);
    }

    public @Nullable Visual getVisual() {
        return visual;
    }

    public void setVisual(@Nullable Visual visual) {
        this.visual = visual;
    }

    public @Nullable Rectangle getCollider() {
        if (collider == null) return null;

        float tileSize = EnvironmentConfig.get().getTileSize();
        return new Rectangle(
            getWorldX(),
            getWorldY(),
            collider.width * tileSize,
            collider.height * tileSize
        );
    }

    public @Nullable Vector2 getColliderCenter() {
        if (collider == null) return null;

        return new Vector2(
            getTileX() + collider.width / 2f,
            getTileY() + collider.height / 2f
        );
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

    public void forEachPart(Consumer<EntityPart> action) {
        action.accept(this);
        for (EntityPart child : children) {
            child.forEachPart(action);
        }
    }

    @Override
    public Iterator<EntityPart> iterator() {
        return children.iterator();
    }
}
