package com.redsponge.sponge.post;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.redsponge.sponge.assets.Assets;

public class ShaderEffect implements PostProcessingEffect {

    private final ShaderProgram shaderProgram;

    public ShaderEffect(String shaderName) {
        shaderProgram = Assets.get().getCommon().getShader(shaderName);
    }

    @Override
    public void process(SpriteBatch batch, TextureRegion buffer) {
        batch.setShader(shaderProgram);
        batch.begin();
        batch.draw(buffer, 0, 0, buffer.getRegionWidth(), buffer.getRegionHeight());
        batch.end();
    }
}
