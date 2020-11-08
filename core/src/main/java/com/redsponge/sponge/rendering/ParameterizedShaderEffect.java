package com.redsponge.sponge.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.util.UGL;

import java.util.HashMap;

public class ParameterizedShaderEffect extends ShaderEffect {

    private HashMap<String, Object> params = new HashMap<>();

    public ParameterizedShaderEffect(String shaderName) {
        super(shaderName);
    }

    public ParameterizedShaderEffect(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    @Override
    public void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer) {
        batch.setShader(shaderProgram);
        batch.begin();

        shaderProgram.setUniformf("u_width", viewport.getWorldWidth());
        shaderProgram.setUniformf("u_height", viewport.getWorldHeight());
        for (String name : params.keySet()) {
            Object obj = params.get(name);
            UGL.setUniformObject(shaderProgram, name, obj);
        }

        batch.draw(buffer, 0, 0);
        batch.end();
    }

    public void putParameter(String name, Object value) {
        params.put(name, value);
    }
}
