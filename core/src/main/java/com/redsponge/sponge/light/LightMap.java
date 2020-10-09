package com.redsponge.sponge.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.sponge.SpongeGame;

public class LightMap implements Disposable {

    private FrameBuffer fbo;
    private final TextureRegion region;
    private final FitViewport gameViewport;
    private final Array<Light> lights;
    private final Color backgroundColor;

    private ScalingViewport drawingViewport;
    private int srcBlend, dstBlend;

    public LightMap(FitViewport gameViewport, Color backgroundColor, LightBlending blending) {
        this.gameViewport = gameViewport;
        this.backgroundColor = backgroundColor.cpy();
        this.backgroundColor.a = 0.5f;
        this.region = new TextureRegion();
        this.lights = new Array<>();
        this.drawingViewport = new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        createFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.srcBlend = blending.getSrc();
        this.dstBlend = blending.getDst();
    }

    public void prepareMap() {
        fbo.begin();
        SpongeGame.i().getBatch().begin();
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        gameViewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(gameViewport.getCamera().combined);

        for (Light light : lights) {
            light.render(SpongeGame.i().getBatch());
        }
//        SpongeGame.i().getShapeDrawer().filledRectangle(0, 0, gameViewport.getWorldWidth(), drawingViewport.getScreenHeight(), Color.RED);
        SpongeGame.i().getBatch().end();
        fbo.end();
    }

    public void renderToScreen() {
        drawingViewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(drawingViewport.getCamera().combined);

        SpongeGame.i().getBatch().setBlendFunction(srcBlend, dstBlend);
        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().draw(region, 0, 0, drawingViewport.getWorldWidth(), drawingViewport.getScreenHeight());
        SpongeGame.i().getBatch().end();
        SpongeGame.i().getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void resize(int width, int height) {
        disposeFBO();
        createFBO(width, height);
    }

    private void createFBO(int width, int height) {
        fbo = new FrameBuffer(Format.RGBA8888, width, height, true);
        region.setRegion(fbo.getColorBufferTexture());
        region.flip(false, true);
        drawingViewport = new ScalingViewport(Scaling.fill, width, height);
        drawingViewport.update(width, height, true);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void removeLight(Light light) {
        lights.removeValue(light, true);
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

    public int getSourceBlend() {
        return srcBlend;
    }

    public LightMap setSourceBlend(int sourceBlend) {
        this.srcBlend = sourceBlend;
        return this;
    }

    public int getDestinationBlend() {
        return dstBlend;
    }

    public LightMap setDestinationBlend(int destinationBlend) {
        this.dstBlend = destinationBlend;
        return this;
    }
}
