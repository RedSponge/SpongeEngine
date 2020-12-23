package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.particles.Particle;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.test.Player;
import com.redsponge.sponge.test.PresentationSettings;
import com.redsponge.sponge.test.StaticBackground;

public class PresentationScene extends Scene {

    private float time;
    private FitViewport guiViewport;
    private BitmapFont font;

    public PresentationScene() {
        EventBus.getInstance().registerListener(this);
        Player p;
        add(new StaticBackground());
        add(new MapEntity());
        add(new Enemy(new Vector2(500, 400)));
        add(p = new Player(new Vector2(100, 300)));
        add(new CameraHandler(p));
        add(new ParticleRendererEntity());

        guiViewport = new FitViewport(getWidth(), getHeight());
        font = new BitmapFont(Gdx.files.internal("cool.fnt"));
    }

    @Override
    public void update(float delta) {
        if (time > 0) {
            time -= delta;
            return;
        }

        super.update(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            PresentationSettings.doShake = !PresentationSettings.doShake;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            PresentationSettings.doFreeze = !PresentationSettings.doFreeze;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            PresentationSettings.doParticles = !PresentationSettings.doParticles;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            PresentationSettings.doSound = !PresentationSettings.doSound;
        }
        for (Particle value : assets.getParticleEffectMap().values()) {
            value.update(delta);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();


        guiViewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(guiViewport.getCamera().combined);

        SpongeGame.i().getBatch().begin();
        if(PresentationSettings.doShake)  font.draw(SpongeGame.i().getBatch(), "Screen Shake", 20, guiViewport.getWorldHeight() - 32+10);
        if(PresentationSettings.doFreeze) font.draw(SpongeGame.i().getBatch(), "Freeze", 20, guiViewport.getWorldHeight() - 52+10);
        if(PresentationSettings.doParticles) font.draw(SpongeGame.i().getBatch(), "Particles", 20, guiViewport.getWorldHeight() - 72+10);
        if(PresentationSettings.doSound) font.draw(SpongeGame.i().getBatch(), "Sound", 20, guiViewport.getWorldHeight() - 92+10);
        SpongeGame.i().getBatch().end();
    }

    @EventHandler
    public void onRequireFreezeEvent(RequireFreezeEvent event) {
        if(!PresentationSettings.doFreeze) return;
        time = event.time;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        guiViewport.update(width, height, true);
    }

    @Override
    public int getWidth() {
        return 320 * 2;
    }

    @Override
    public int getHeight() {
        return 180 * 2;
    }

    @Override
    public String getName() {
        return "test";
    }
}
