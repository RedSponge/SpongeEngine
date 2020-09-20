package com.redsponge.sponge;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.test.TestScene;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpongeGame implements ApplicationListener {

    private long startTime;
    private static SpongeGame instance;

    private SpriteBatch spriteBatch;
    private ShapeDrawer shapeDrawer;
    private Texture shapeDrawerTextures;
    private AssetManager assetManager;

    private Scene scene;

    private Map<Class<?>, List<Class<?>>> entityClasses;
    private Map<Class<?>, List<Class<?>>> componentClasses;

    public static SpongeGame i() {
        return instance;
    }

    @Override
    public final void create() {
        startTime = System.currentTimeMillis();
        spriteBatch = new SpriteBatch();
        initializeShapeDrawer();
        assetManager = new AssetManager();

        entityClasses = new HashMap<>();
        componentClasses = new HashMap<>();

        instance = this;
        scene = null;

        init();
        setScene(new TestScene());
    }

    protected void init() {}

    public void setScene(TestScene scene) {
        if(this.scene != null) this.scene.dispose();
        this.scene = scene;
        scene.start();
    }

    private void initializeShapeDrawer() {
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        shapeDrawerTextures = new Texture(pixmap);
        pixmap.dispose();

        shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(shapeDrawerTextures));
    }

    public void registerEntityType(Class<?> entity) {
        if(entity == Object.class || entityClasses.containsKey(entity)) return;
        List<Class<?>> classes = new ArrayList<>();

        classes.add(entity);
        Collections.addAll(classes, (Class<?>[]) ClassReflection.getInterfaces(entity));
        if(entity.getSuperclass() != Object.class) {
            registerEntityType(entity.getSuperclass());
            classes.addAll(entityClasses.get(entity.getSuperclass()));
        }
        entityClasses.put(entity, classes);
    }

    public void registerComponentType(Class<?> component) {
        if(component == Object.class || componentClasses.containsKey(component)) return;
        List<Class<?>> classes = new ArrayList<>();

        classes.add(component);
        Collections.addAll(classes, (Class<?>[]) ClassReflection.getInterfaces(component));
        if(component.getSuperclass() != Object.class) {
            registerComponentType(component.getSuperclass());
            classes.addAll(componentClasses.get(component.getSuperclass()));
        }
        componentClasses.put(component, classes);
    }

    public float getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000f;
    }

    @Override
    public void render() {
        if(scene == null) {
            Gdx.gl.glClearColor(0, 0, 0, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }
        final float delta = Gdx.graphics.getDeltaTime();
        scene.update(delta);
        renderScene(scene);
    }

    private void renderScene(Scene scene) {
        scene.viewport.apply();
        spriteBatch.setProjectionMatrix(scene.viewport.getCamera().combined);

        spriteBatch.begin();
        scene.render();
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        if(scene != null) scene.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeDrawerTextures.dispose();
    }

    public List<Class<?>> getEntityClasses(Class<?> entity) {
        registerEntityType(entity);
        return entityClasses.get(entity);
    }

    public List<Class<?>> getComponentClasses(Class<?> component) {
        registerComponentType(component);
        return componentClasses.get(component);
    }

    public SpriteBatch getBatch() {
        return spriteBatch;
    }

    public ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}