package com.redsponge.sponge.post;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.assets.Assets;

public class ShaderEffect implements RenderingEffect {

    private final ShaderProgram shaderProgram;

    public ShaderEffect(String shaderName) {
        shaderProgram = Assets.get().getCommon().getShader(shaderName);
    }

    public ShaderEffect(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        batch.setShader(shaderProgram);
        batch.begin();

        shaderProgram.setUniformf("u_width", viewport.getWorldWidth());
        shaderProgram.setUniformf("u_height", viewport.getWorldHeight());

        batch.draw(buffer, 0, 0);
        batch.end();
    }
}
