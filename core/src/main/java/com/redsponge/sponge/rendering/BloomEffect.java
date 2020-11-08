package com.redsponge.sponge.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class BloomEffect extends RenderingEffect implements Disposable, Resizable {

    private final Array<Runnable> bloomRenderings;
    private final FitViewport drawViewport;
    private final RenderingPipeline rPipeline;
    private final float downscaleFactor;

    public BloomEffect(boolean isActive, SpriteBatch batch, FitViewport sceneViewport, float downscaleFactor) {
        super(isActive);
        this.drawViewport = new FitViewport(sceneViewport.getWorldWidth(), sceneViewport.getWorldHeight());
        this.downscaleFactor = downscaleFactor;

        int downscaleWidth = (int) (sceneViewport.getWorldWidth() / downscaleFactor);
        int downscaleHeight = (int) (sceneViewport.getWorldHeight() / downscaleFactor);

        this.drawViewport.update(downscaleWidth, downscaleHeight, true);
        rPipeline = new RenderingPipeline(batch, downscaleWidth, downscaleHeight);

        bloomRenderings = new Array<>();
        Effects.addGaussianBlur(rPipeline);
    }

    public void addBloomRender(Runnable runnable) {
        bloomRenderings.add(runnable);
    }

    @Override
    void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        rPipeline.beginCapture();
        drawViewport.apply();
        batch.setProjectionMatrix(drawViewport.getCamera().combined);
//        UGL.getFboStack().push(bloomFBO);
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
//        batch.setColor(new Color(1, 0, 0, 1));
        for (int i = 0; i < bloomRenderings.size; i++) {
            bloomRenderings.get(i).run();
        }
        batch.end();
        rPipeline.endCapture();
        TextureRegion bloomMap = rPipeline.applyEffects();
//
//        UGL.getFboStack().push(bloomFBO);
//        Gdx.gl.glClearColor(0, 0, 0, 0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        rPipeline.drawToScreen();
//        if(UGL.getFboStack().pop() != bloomFBO) {
//            Logger.warn(this, "using rPipeline.drawToScreen() didn't clean all fbos it binded!");
//        };
//
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.setColor(Color.WHITE);
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(buffer, 0, 0);
//        Logger.info(this, bloomFBORegion.getRegionWidth(), bloomFBORegion.getRegionHeight());
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
////        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(bloomMap, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.end();
    }

    public void dispose() {
        rPipeline.dispose();
    }

    @Override
    public void resize(int width, int height) {
        rPipeline.resize(width, height);
    }
}
