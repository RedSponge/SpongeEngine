package com.redsponge.sponge.post;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public interface PostProcessingEffect {

    void process(SpriteBatch batch, TextureRegion buffer);

}
