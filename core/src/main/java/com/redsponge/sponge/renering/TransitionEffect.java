package com.redsponge.sponge.renering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.util.UGL;

public class TransitionEffect implements RenderingEffect {

    public static final int TRANSITION_TEXTURE_ID = 16;

    private final ShaderProgram shader;
    private final Texture transitionTexture;
    private final Color transitionColor;

    private float progress;
    private float fadePercent;

    public TransitionEffect(Texture transitionTexture) {
        this.transitionTexture = transitionTexture;
        this.shader = Assets.get().getCommon().getShader("transition");
        this.progress = 0;
        this.transitionColor = Color.BLACK.cpy();
        this.fadePercent = 0;
    }

    @Override
    public void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        batch.setShader(shader);
        batch.begin();

        transitionTexture.bind(TRANSITION_TEXTURE_ID);
        shader.setUniformi("u_transitionTexture", TRANSITION_TEXTURE_ID);
        shader.setUniformf("u_progress", progress);
        UGL.setUniformColour(shader, "u_transitionColour", transitionColor);
        shader.setUniformf("u_fade", fadePercent);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.draw(buffer, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
    }

    public float getProgress() {
        return progress;
    }

    public TransitionEffect setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public float getFadePercent() {
        return fadePercent;
    }

    public TransitionEffect setFadePercent(float fadePercent) {
        this.fadePercent = fadePercent;
        return this;
    }

    public Color getTransitionColor() {
        return transitionColor;
    }
}
