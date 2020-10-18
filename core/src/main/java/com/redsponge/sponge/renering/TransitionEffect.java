package com.redsponge.sponge.renering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.assets.Assets;

public class TransitionEffect implements RenderingEffect {

    public static final int TRANSITION_TEXTURE_ID = 16;

    private final ShaderProgram shader;
    private final TextureRegion transitionTexture;

    private float progress;

    public TransitionEffect(TextureRegion transitionTexture) {
        this.transitionTexture = transitionTexture;
        this.shader = Assets.get().getCommon().getShader("transition");
    }

    @Override
    public void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        batch.setShader(shader);
        batch.begin();

        transitionTexture.getTexture().bind(TRANSITION_TEXTURE_ID);
        shader.setUniformi("u_transitionTexture", TRANSITION_TEXTURE_ID);
        shader.setUniformf("u_progress", progress);

        batch.draw(buffer, 0, 0);
        batch.end();
    }

    public float getProgress() {
        return progress;
    }

    public TransitionEffect setProgress(float progress) {
        this.progress = progress;
        return this;
    }
}
