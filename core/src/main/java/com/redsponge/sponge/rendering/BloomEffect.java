package com.redsponge.sponge.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.util.Logger;
import com.redsponge.sponge.util.UGL;

public class BloomEffect extends RenderingEffect implements Disposable, Resizable {

    private final FrameBuffer bloomFBO;
    private final TextureRegion bloomFBORegion;

    private final Array<Runnable> bloomRenderings;
    private final FitViewport sceneViewport;
    private final RenderingPipeline rPipeline;

    public BloomEffect(boolean isActive, SpriteBatch batch, FitViewport sceneViewport) {
        super(isActive);
        this.sceneViewport = sceneViewport;
        bloomFBO = UGL.createFrameBuffer((int) sceneViewport.getWorldWidth(), (int) sceneViewport.getWorldHeight(), false, false);
        bloomFBORegion = new TextureRegion(bloomFBO.getColorBufferTexture());
        bloomFBORegion.flip(false, true);
        rPipeline = new RenderingPipeline(batch, (int) sceneViewport.getWorldWidth(), (int) sceneViewport.getWorldHeight());
        bloomRenderings = new Array<>();
        Effects.addGaussianBlur(rPipeline);
    }

    public void addBloomRender(Runnable runnable) {
        bloomRenderings.add(runnable);
    }

    @Override
    void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        rPipeline.beginCapture();
//        UGL.getFboStack().push(bloomFBO);
        Gdx.gl.glClearColor(1, 0, 0, .1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
//        batch.setColor(new Color(1, 0, 0, 1));
        for (int i = 0; i < bloomRenderings.size; i++) {
            bloomRenderings.get(i).run();
        }
        batch.end();
        rPipeline.endCapture();
        rPipeline.drawToScreen();
//
//        UGL.getFboStack().push(bloomFBO);
//        Gdx.gl.glClearColor(0, 0, 0, 0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        rPipeline.drawToScreen();
        if(UGL.getFboStack().pop() != bloomFBO) {
            Logger.warn(this, "using rPipeline.drawToScreen() didn't clean all fbos it binded!");
        };
//
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.setColor(Color.WHITE);
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(buffer, 0, 0);
//        Logger.info(this, bloomFBORegion.getRegionWidth(), bloomFBORegion.getRegionHeight());
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
////        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(bloomFBORegion, 0, 0);
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.end();
    }

    public void dispose() {
        bloomFBO.dispose();
        rPipeline.dispose();
    }

    @Override
    public void resize(int width, int height) {
        rPipeline.resize(width, height);
    }
}
