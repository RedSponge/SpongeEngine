package com.redsponge.sponge.game.win;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.screen.Scene;

public class WinScene extends Scene {

    @Override
    public void start() {
        super.start();
        add(new WinBackground());

        sceneTransitionEffect.setTransitionTexture(Assets.get().getCommon().getTransitionTexture("slide_down.png"));
        getRenderingPipeline().addEffect(sceneTransitionEffect);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 0 / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void update(float delta) {
        float time = SpongeGame.i().getElapsedTime() - getStartTime();
        if(time < 0.1f) {
            sceneTransitionEffect.setProgress((float) Math.sin(1 - (time / 0.1f)));
        } else {
            sceneTransitionEffect.setProgress(0);
        }
        super.update(delta);
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 320;
    }

    @Override
    public String getName() {
        return "win";
    }
}
