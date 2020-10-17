package com.redsponge.sponge.post;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.util.UGwt;

public class RenderingPipeline implements Disposable {

    private final Array<RenderingEffect> effects;
    private final FrameBuffer pongFBO; // Ping-ponging with primary buffer when doing post-processing
    private final FrameBuffer mainFBO;

    private final TextureRegion mainRegion;
    private final TextureRegion pongRegion;

    private final FitViewport gameViewport;
    private final FitViewport copyViewport;
    private final FitViewport toScreenViewport;
    private final SpriteBatch batch;

    private final ShaderProgram defaultShader;

    public RenderingPipeline(SpriteBatch batch, int width, int height) {
        this.batch = batch;

        this.gameViewport = new FitViewport(width, height);
        this.copyViewport = new FitViewport(width, height);
        this.toScreenViewport = new FitViewport(width, height);

        this.gameViewport.update(width, height, true);
        this.copyViewport.update(width, height, true);

        this.mainFBO = UGwt.createFrameBuffer(width, height, false, false);
        this.pongFBO = UGwt.createFrameBuffer(width, height, false, false);

        this.mainFBO.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        this.pongFBO.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        this.mainRegion = new TextureRegion(this.mainFBO.getColorBufferTexture());
        this.pongRegion = new TextureRegion(this.pongFBO.getColorBufferTexture());

        this.mainRegion.flip(false, true);
        this.pongRegion.flip(false, true);

        this.defaultShader = SpriteBatch.createDefaultShader();

        this.effects = new Array<>();
    }

    public void beginCapture() {
        mainFBO.begin();

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
    }

    public void endCapture() {
        mainFBO.end();
    }

    public void drawToScreen() {
        TextureRegion[] regions = {pongRegion, mainRegion};
        FrameBuffer[] buffers = {pongFBO, mainFBO};
        int idx = 0;

        batch.setProjectionMatrix(copyViewport.getCamera().combined);
        copyViewport.apply();

        for (int i = 0; i < effects.size; i++) {
            buffers[idx].begin();
            Gdx.gl.glClearColor(0, 0, 0, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            effects.get(i).apply(copyViewport, batch, regions[1 - idx]);
            buffers[idx].end();
            idx = 1 - idx;
        }

        toScreenViewport.apply();
        batch.setProjectionMatrix(toScreenViewport.getCamera().combined);
        batch.setShader(defaultShader);
        batch.begin();
        batch.draw(regions[1 - idx], 0, 0, toScreenViewport.getWorldWidth(), toScreenViewport.getWorldHeight());
        batch.end();
    }

    public void resize(int width, int height) {
        toScreenViewport.update(width, height, true);
    }

    public void addEffect(RenderingEffect effect) {
        effects.add(effect);
    }

    public void removeEffect(RenderingEffect effect) {
        effects.removeValue(effect, true);
    }

    @Override
    public void dispose() {
        mainFBO.dispose();
        pongFBO.dispose();
    }

    public FitViewport getGameViewport() {
        return gameViewport;
    }

    public FitViewport getCopyViewport() {
        return copyViewport;
    }

    public FitViewport getToScreenViewport() {
        return toScreenViewport;
    }
}
