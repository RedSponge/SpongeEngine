package com.redsponge.sponge.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.UVector;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class Entity {

    private Scene scene;
    private int priority;
    private boolean active;
    private boolean visible;
    private boolean collidable;
    private boolean deleteOnRemove;

    private List<Component> components;
    private Set<Component> toAdd;
    private Set<Component> toRemove;

    private Vector2 position;
    private int zIndex;
    private Hitbox hitbox;

    public Entity(Vector2 pos) {
        this.position = pos;

        components = new ArrayList<>();
        toAdd = new LinkedHashSet<>();
        toRemove = new LinkedHashSet<>();

        active = true;
        visible = true;
        collidable = true;
        deleteOnRemove = true;

        hitbox = new Hitbox();
    }

    public void added(Scene scene) {
        this.scene = scene;
    }

    public void removed() {
        this.scene = null;
    }

    public void begin() {
        for (Component component : components) {
            component.begin();
        }
    }

    public void update(float delta) {
        updateLists();
        for (Component component : components) {
            if(component.isActive()) {
                component.update(delta);
            }
        }
    }

    public void render() {
        for (Component component : components) {
            if(component.isVisible()) {
                component.render();
            }
        }
    }

    public void removeSelf() {
        if(scene != null) scene.remove(this);
    }

    public <T extends Component> T add(T component) {
        if(component.getEntity() == null) {
            toAdd.add(component);
        }
        return component;
    }

    public <T extends Component> T remove(T component) {
        if(component.getEntity() == this) {
            toRemove.add(component);
        }
        return component;
    }

    public void updateLists() {
        if(toRemove.size() > 0) {
            for (Component component : toRemove) {
                components.remove(component);
                scene.untrackComponent(component);
                component.removed();
            }
            toRemove.clear();
        }

        if(toAdd.size() > 0) {
            for (Component component : toAdd) {
                components.add(component);
                scene.trackComponent(component);
                component.added(this);
            }
            toAdd.clear();
        }
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
        if(scene != null) scene.requireZSort();
    }

    public int getzIndex() {
        return zIndex;
    }

    public Vector2 getPositionf() {
        return position;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public float getXf() {
        return position.x;
    }

    public float getYf() {
        return position.y;
    }

    public int getX() {
        return Math.round(position.x);
    }

    public int getY() {
        return Math.round(position.y);
    }

    public Vector2 getPosition() {
        return UVector.toInt(position);
    }

    public int getWidth() {
        return hitbox.getWidth();
    }

    public void setWidth(int width) {
        hitbox.setWidth(width);
    }

    public void setHeight(int height) {
        hitbox.setHeight(height);
    }

    public int getHeight() {
        return hitbox.getHeight();
    }

    public Hitbox getSceneHitbox() {
        return hitbox.add(position);
    }

    public int getLeft() {
        return (int) (position.x + hitbox.getLeft());
    }

    public void setLeft(int left) {
        position.x = left - hitbox.getLeft();
    }

    public int getRight() {
        return (int) (position.x + hitbox.getRight());
    }

    public void setRight(int right) {
        position.x = right - hitbox.getRight();
    }

    public int getTop() {
        return (int) (position.y + hitbox.getTop());
    }

    public void setTop(int top) {
        position.y = top - hitbox.getTop();
    }

    public int getBottom() {
        return (int) (position.y + hitbox.getBottom());
    }

    public void setBottom(int bottom) {
        position.x = bottom - hitbox.getBottom();
    }

    public boolean check(Vector2 point) {
        return getSceneHitbox().contains(point);
    }

    public boolean check(Hitbox hb) {
        return getSceneHitbox().intersects(hb);
    }

    public boolean check(Entity other) {
        return other.collidable && getSceneHitbox().intersects(other.getSceneHitbox());
    }

    public boolean check(Entity other, Vector2 offset) {
        return other.collidable && (getSceneHitbox().add(offset)).intersects(other.getSceneHitbox());
    }

    public boolean checkOutside(Entity other, Vector2 offset) {
        return other.isCollidable() && !getSceneHitbox().intersects(other.getSceneHitbox()) && getSceneHitbox().add(offset).intersects(other.getSceneHitbox());
    }

    public <T extends Entity> boolean check(Class<T> clazz) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Entity> boolean check(Class<T> clazz, Vector2 point) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity, point)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Entity> boolean checkOutside(Class<T> clazz, Vector2 offset) {
        for (T t : scene.all(new ArrayList<>(), clazz)) {
            if(checkOutside(t, offset)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Entity> T first(Class<T> clazz) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity)) {
                return entity;
            }
        }
        return null;
    }

    public <T extends Entity> T first(Class<T> clazz, Vector2 point) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity, point)) {
                return entity;
            }
        }
        return null;
    }

    public <T extends Entity> T firstOutside(Class<T> clazz, Vector2 offset) {
        for (T t : scene.all(new ArrayList<>(), clazz)) {
            if(checkOutside(t, offset)) {
                return t;
            }
        }
        return null;
    }

    public <T extends Entity> List<T> all(List<T> into, Class<T> clazz) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity)) {
                into.add(entity);
            }
        }
        return into;
    }

    public <T extends Entity> List<T> all(List<T> into, Vector2 point, Class<T> clazz) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(check(entity, point)) {
                into.add(entity);
            }
        }
        return into;
    }

    public <T extends Entity> List<T> allOutside(List<T> into, Vector2 point, Class<T> clazz) {
        for (T entity : scene.all(new ArrayList<>(), clazz)) {
            if(checkOutside(entity, point)) {
                into.add(entity);
            }
        }
        return into;
    }

    public void drawHitbox(ShapeDrawer sr) {
        Hitbox hitbox = getSceneHitbox();
        sr.rectangle(hitbox.getLeft(), hitbox.getBottom(), hitbox.getWidth(), hitbox.getHeight(), Color.WHITE);
    }


    public Hitbox getHitbox() {
        return hitbox;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public List<Component> getComponents() {
        return components;
    }
}
