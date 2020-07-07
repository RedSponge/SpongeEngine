package com.redsponge.sponge;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpongeGame extends ApplicationAdapter {

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 1, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}