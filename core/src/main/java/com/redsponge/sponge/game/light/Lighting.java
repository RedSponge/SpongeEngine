package com.redsponge.sponge.game.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.sponge.SpongeGame;

import java.util.ArrayList;
import java.util.List;

public class Lighting implements Disposable {

    private FrameBuffer lightFbo;
    private FitViewport viewport;

    private List<Light> lights;
    private ScalingViewport sv;
    private TextureRegion fboRegion;

    public Lighting(FitViewport viewport) {
        this.viewport = viewport;
        lights = new ArrayList<>();
        fboRegion = new TextureRegion();
        fboRegion.flip(false, true);
        createFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void drawLights() {
        lightFbo.begin();
        Gdx.gl.glClearColor(0, 0.1f,  0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(viewport.getCamera().combined);
        SpongeGame.i().getBatch().begin();
        for (Light light : lights) {
            light.render(SpongeGame.i().getBatch());
        }
        SpongeGame.i().getBatch().end();
        lightFbo.end();
    }

    public void drawOnScreen() {
        viewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(viewport.getCamera().combined);
        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_COLOR);
        SpongeGame.i().getBatch().draw(fboRegion, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        SpongeGame.i().getBatch().end();
        SpongeGame.i().getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void resize(int width, int height) {
        if(lightFbo != null) {
            lightFbo.dispose();
        }
        createFBO(width, height);
    }

    private void createFBO(int width, int height) {
        lightFbo = new FrameBuffer(Format.RGBA8888, width, height, false);
        fboRegion.setRegion(lightFbo.getColorBufferTexture());
        fboRegion.flip(false, true);
    }

    @Override
    public void dispose() {
        if(lightFbo != null) {
            lightFbo.dispose();
            lightFbo = null;
        }
    }

    public void removeLight(Light light) {
        lights.remove(light);
    }
}
