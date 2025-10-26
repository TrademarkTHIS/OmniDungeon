package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.entities.Entity;

public abstract class AbstractController implements Controller {
    protected Entity entity;
    private boolean active = false;

    public void bind(Entity entity) {
        if (active) throw new IllegalStateException("Controller already bound!");
        this.entity = entity;
        this.active = true;
    }

    public void unbind() {
        this.entity = null;
        this.active = false;
    }

    public Entity getEntity() {
        if (!active) throw new IllegalStateException("Controller is unbound!");
        return entity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

