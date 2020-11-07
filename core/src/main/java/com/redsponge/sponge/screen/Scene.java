package com.redsponge.sponge.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.assets.SceneAssets;
import com.redsponge.sponge.entity.Component;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.renering.RenderingPipeline;
import com.redsponge.sponge.renering.TransitionEffect;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Scene {

    private float startTime;

    private final Set<Entity> toAdd;
    private final Set<Entity> toRemove;
    private final List<Entity> entities;
    private final Map<Class<?>, List<Entity>> entitiesByType;
    private final Map<Class<?>, List<Component>> componentsByType;

    protected SceneAssets assets;

    protected RenderingPipeline rPipeline;
    protected TransitionEffect sceneTransitionEffect;
    protected boolean isFirstUpdate;

    public Scene() {
        rPipeline = new RenderingPipeline(SpongeGame.i().getBatch(), getWidth(), getHeight());
        sceneTransitionEffect = new TransitionEffect(null);

        toAdd = new LinkedHashSet<>();
        toRemove = new LinkedHashSet<>();
        entities = new ArrayList<>();
        entitiesByType = new HashMap<>();
        componentsByType = new HashMap<>();

        assets = Assets.get().loadScene(getName());
    }

    public void start() {
        startTime = SpongeGame.i().getElapsedTime();
        isFirstUpdate = true;
    }

    public void update(float delta) {
        updateLists();
        for (Entity entity : entities) {
            if(entity.isActive()) {
                entity.update(delta);
            }
        }

        if(isFirstUpdate) {
            if(!rPipeline.contains(sceneTransitionEffect)) {
                Logger.warn(this, "sceneTransitionEffect wasn't added to pipeline! Make sure you add it for transitions to work properly!");
            }
            isFirstUpdate = false;
        }
    }
    public void render() {
        rPipeline.beginCapture();
        SpongeGame.i().getBatch().begin();
        for (Entity entity : entities) {
            if(entity.isVisible()) {
                entity.render();
            }
        }
        SpongeGame.i().getBatch().end();
        rPipeline.endCapture();
        rPipeline.drawToScreen();
    }

    public <T extends Entity> T add(T entity) {
        if(entity.getScene() == null) {
            toAdd.add(entity);
        }
        return entity;
    }

    public <T extends Entity> T remove(T entity) {
        if(entity.getScene() == this) {
            toRemove.add(entity);
        }
        return entity;
    }



    private void updateLists() {
        if(toRemove.size() > 0) {
            for (Entity entity : toRemove) {
                entities.remove(entity);
                untrackEntity(entity);
                entity.removed();
            }
            toRemove.clear();
        }

        if(toAdd.size() > 0) {
            for (Entity entity : toAdd) {
                entities.add(entity);
                trackEntity(entity);
                entity.added(this);
            }
        }

        for (Entity entity : entities) {
            entity.updateLists();
        }
        
        if(toAdd.size() > 0) {
            for (Entity entity : toAdd) {
                entity.begin();
            }
            toAdd.clear();
        }
    }

    private void trackEntity(Entity entity) {
        for (Class<?> entityClass : SpongeGame.i().getEntityClasses(entity.getClass())) {
            if(!entitiesByType.containsKey(entityClass)) entitiesByType.put(entityClass, new ArrayList<>());
            entitiesByType.get(entityClass).add(entity);
        }

        for (Component component : entity.getComponents()) {
            trackComponent(component);
        }
    }

    private void untrackEntity(Entity entity) {
        for (Class<?> entityClass : SpongeGame.i().getEntityClasses(entity.getClass())) {
            entitiesByType.get(entityClass).remove(entity);
        }

        for (Component component : entity.getComponents()) {
            untrackComponent(component);
        }
    }


    public void trackComponent(Component component) {
        for (Class<?> componentClass : SpongeGame.i().getComponentClasses(component.getClass())) {
            if(!componentsByType.containsKey(componentClass)) componentsByType.put(componentClass, new ArrayList<>());
            componentsByType.get(componentClass).add(component);
        }
    }

    public void untrackComponent(Component component) {
        for (Class<?> componentClass : SpongeGame.i().getComponentClasses(component.getClass())) {
            componentsByType.get(componentClass).remove(component);
        }
    }

    public <T extends Entity> int count(Class<T> clazz) {
        return entitiesByType.get(clazz).size();
    }

    public <T extends Entity> T first(Class<T> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            return (T) entity;
        }
        return null;
    }

    public <T extends Entity> T first(Vector2 point, Class<? extends Entity> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            if(entity.check(point)) {
                return (T) entity;
            }
        }
        return null;
    }

    public <T extends Entity> T first(Hitbox hb, Class<? extends Entity> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            if(entity.check(hb)) {
                return (T) entity;
            }
        }
        return null;
    }

    public <T extends Entity> List<T> all(List<T> into, Class<T> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            into.add((T) entity);
        }
        return into;
    }

    public <T extends Entity> List<T> all(Vector2 point, List<T> into, Class<T> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            if(entity.check(point))
                into.add((T) entity);
        }
        return into;
    }

    public <T extends Entity> List<T> all(Hitbox hb, List<T> into, Class<T> clazz) {
        if(!entitiesByType.containsKey(clazz)) entitiesByType.put(clazz, new ArrayList<>());
        for (Entity entity : entitiesByType.get(clazz)) {
            if(entity.check(hb))
                into.add((T) entity);
        }
        return into;
    }



    public abstract int getWidth();
    public abstract int getHeight();

    public void resize(int width, int height) {
        rPipeline.resize(width, height);
    }

    public float getStartTime() {
        return startTime;
    }

    public void dispose() {
        Assets.get().unloadScene(getName());
        rPipeline.dispose();
    }

    public FitViewport getViewport() {
        return rPipeline.getGameViewport();
    }

    public abstract String getName();

    public SceneAssets getAssets() {
        return assets;
    }
}
