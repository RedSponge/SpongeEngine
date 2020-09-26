package com.redsponge.sponge.game.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
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

    public Lighting() {
        viewport = new FitViewport(480, 270);
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        viewport.apply();
//        SpongeGame.i().getBatch().setProjectionMatrix(viewport.getCamera().combined);
//        SpongeGame.i().getBatch().begin();
//        for (Light light : lights) {
//            light.render(SpongeGame.i().getBatch());
//        }
//        SpongeGame.i().getBatch().end();
        lightFbo.end();
    }

    public void drawOnScreen() {
        sv.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(sv.getCamera().combined);
        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().draw(lightFbo.getColorBufferTexture(), 0, 0, sv.getScreenWidth(), sv.getScreenHeight());
        SpongeGame.i().getBatch().end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if(lightFbo != null) {
            lightFbo.dispose();
        }
        createFBO(width, height);
    }

    private void createFBO(int width, int height) {
        lightFbo = new FrameBuffer(Format.RGBA8888, width, height, true);
        fboRegion.setRegion(lightFbo.getColorBufferTexture());
        sv = new ScalingViewport(Scaling.fill, width, height);
        sv.apply(true);
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if(lightFbo != null) {
            lightFbo.dispose();
            lightFbo = null;
        }
    }

    public void test() {
        FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        fbo.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fbo.end();

        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().draw(fbo.getColorBufferTexture(), 0, 0, 1000, 1000);
        SpongeGame.i().getBatch().end();

    }
}
