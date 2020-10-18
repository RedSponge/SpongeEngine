package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.renering.Effects;
import com.redsponge.sponge.renering.TransitionEffect;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class TestScene extends Scene {

    private StaticBackground bg;
    private Player pl;

//    private LightMap lightMap;

    @Override
    public void start() {
        super.start();
        add(bg = new StaticBackground());
        add(pl = new Player(new Vector2(100, 100)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, getWidth(), 20)));
        add(new Block(new Vector2(-2, 0), new Hitbox(0, 0, 1, 1000)));
        add(new Block(new Vector2(getWidth() + 1, 0), new Hitbox(0, 0, 1, 1000)));
        add(new JumpThru(new Vector2(100, 50), 100));

        TransitionEffect te = new TransitionEffect(Assets.get().getCommon().getTransitionTextures().findRegion("slide"));

        rPipeline.addEffect(te);
//        Effects.addGaussianBlur(rPipeline);
//        Effects.addGaussianBlur(rPipeline);
//        Effects.addGaussianBlur(rPipeline);
//        Effects.addGreyscale(rPipeline);
//        Effects.addInvert(rPipeline);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public int getWidth() {
        return 640;
    }

    @Override
    public int getHeight() {
        return 360;
    }

    @Override
    public String getName() {
        return "test";
    }
}
