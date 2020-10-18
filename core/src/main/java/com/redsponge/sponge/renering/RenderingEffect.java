package com.redsponge.sponge.renering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;

public interface RenderingEffect {

    void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer);

}
