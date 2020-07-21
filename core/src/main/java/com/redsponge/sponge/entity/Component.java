package com.redsponge.sponge.entity;

public abstract class Component {

    private Entity entity;

    private boolean active;
    private boolean visible;

    public Component(boolean active, boolean visible) {
        this.active = active;
        this.visible = visible;
    }

    public void added(Entity entity) {
        this.entity = entity;
    }

    public void removed() {
        this.entity = null;
    }

    public abstract void begin();
    public abstract void update(float delta);
    public abstract void render();

    public void removeSelf() {
        entity.remove(this);
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
