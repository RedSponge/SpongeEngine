package com.redsponge.sponge.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LightMap implements Disposable {

    private FrameBuffer fbo;
    private TextureRegion region;
    private FitViewport gameViewport;
    private Array<Light> lights;
    private Color backgroundColor;

    private FitViewport drawingViewport;

    public LightMap(FitViewport gameViewport, Color backgroundColor, int width, int height) {
        this.gameViewport = gameViewport;
        this.backgroundColor = backgroundColor.cpy();
        this.region = new TextureRegion();
        this.lights = new Array<>();
        this.drawingViewport = new FitViewport(width, height);
        createFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void prepareMap(SpriteBatch batch) {
        fbo.begin();
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
        for (Light light : lights) {
            light.render(batch);
        }
        batch.end();

        fbo.end();
    }

    public void renderToScreen(SpriteBatch batch) {
        drawingViewport.apply();
        batch.setProjectionMatrix(drawingViewport.getCamera().combined);
        batch.begin();
        batch.draw(region, 0, 0, region.getRegionWidth(), region.getRegionHeight());
        batch.end();
    }

    public void resize(int width, int height) {
        disposeFBO();
        createFBO(width, height);

        drawingViewport.update(width, height, true);
    }

    private void createFBO(int width, int height) {
        fbo = new FrameBuffer(Format.RGBA8888, width, height, true);
        region.setRegion(fbo.getColorBufferTexture());
        region.flip(false, true);
    }


    private void disposeFBO() {
        if(fbo != null) {
            fbo.dispose();
            fbo = null;
        }
    }

    @Override
    public void dispose() {
        disposeFBO();
    }
}
