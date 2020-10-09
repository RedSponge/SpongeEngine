package com.redsponge.sponge.post;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.assets.Assets;

public class PostProcessingPipeline {

    private final Array<PostProcessingEffect> effects;
    private FrameBuffer primaryBuffer;
    private FrameBuffer pongBuffer; // Ping-ponging with primary buffer when doing post-processing
    private ScalingViewport drawnViewport;
    private TextureRegion primaryRegion;
    private TextureRegion pongRegion;

    private final Viewport gameViewport;

    private ShaderProgram defaultShader;

    public PostProcessingPipeline(Viewport gameViewport) {
        this.gameViewport = gameViewport;
        effects = new Array<>();
        primaryRegion = new TextureRegion();
        pongRegion = new TextureRegion();
        defaultShader = SpriteBatch.createDefaultShader();
        createFBOs(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void beginCapture() {
        SpongeGame.i().getBatch().setShader(defaultShader);
        primaryBuffer.begin();
    }

    public void endCapture() {
        primaryBuffer.end();
    }

    public void drawToScreen() {
        drawnViewport.apply();
        SpongeGame.i().getBatch().setProjectionMatrix(drawnViewport.getCamera().combined);
        FrameBuffer[] buffers = {primaryBuffer, pongBuffer};
        TextureRegion[] bufferRegions = {primaryRegion, pongRegion};
        int idx = 0;

        for (PostProcessingEffect effect : effects) {
            buffers[1 - idx].begin();

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            drawnViewport.apply();
            SpongeGame.i().getBatch().setProjectionMatrix(drawnViewport.getCamera().combined);

            effect.process(SpongeGame.i().getBatch(), bufferRegions[idx]);
            buffers[1 - idx].end();
            idx = 1 - idx;
        }

        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().draw(bufferRegions[1 - idx], 0, 0, drawnViewport.getWorldWidth(), drawnViewport.getWorldHeight());
        SpongeGame.i().getBatch().end();
    }


    private void createFBOs(int width, int height) {
        primaryBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, true);
        pongBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, true);

        primaryRegion.setRegion(primaryBuffer.getColorBufferTexture());
        primaryRegion.flip(false, true);

        pongRegion.setRegion(pongBuffer.getColorBufferTexture());
        pongRegion.flip(false, true);

        drawnViewport = new ScalingViewport(Scaling.fill, width, height);
        drawnViewport.update(width, height, true);
    }

    public void resize(int width, int height) {
        disposeFBOs();
        createFBOs(width, height);
    }

    private void disposeFBOs() {
        if(primaryBuffer != null) {
            primaryBuffer.dispose();
            primaryBuffer = null;
        }
        if(pongBuffer != null) {
            pongBuffer.dispose();
            pongBuffer = null;
        }
    }

    public void addEffect(PostProcessingEffect effect) {
        effects.add(effect);
    }
}
