package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.rendering.Effects;
import com.redsponge.sponge.rendering.ParameterizedShaderEffect;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.UMath;

public class TestScene extends Scene {

    private StaticBackground bg;
    private Player pl;
    private float prog;
    private float fade;

    private InputAxis progControl;
    private InputAxis fadeControl;
    private InputAxis pixelationControl;

    private BloomEffect be;
    private ParameterizedShaderEffect pixelationEffect;
    private float pixelation;

    @Override
    public void start() {
        super.start();
        add(bg = new StaticBackground());
        for(int i = 0; i < 8; i++) {
            add(new Torch(new Vector2(40 + i * 80, 100)));
        }
        add(pl = new Player(new Vector2(100, 100)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, getWidth(), 20)));
        add(new Block(new Vector2(-2, 0), new Hitbox(0, 0, 1, 1000)));
        add(new Block(new Vector2(getWidth() + 1, 0), new Hitbox(0, 0, 1, 1000)));
        add(new JumpThru(new Vector2(100, 50), 100));

        be = new BloomEffect(true, SpongeGame.i().getBatch(), getViewport(), 1);
        rPipeline.addEffect(be);

        Effects.addContrast(rPipeline, 0.1f);
        pixelationEffect = Effects.addPixelation(rPipeline, 1);

        sceneTransitionEffect.setTransitionTexture(Assets.get().getCommon().getTransitionTexture("pokemon.png"));
        sceneTransitionEffect.getTransitionColor().set(Color.WHITE);
        rPipeline.addEffect(sceneTransitionEffect);


        progControl = new InputAxis(new InputEntry().addKey(Keys.G), new InputEntry().addKey(Keys.H));
        fadeControl = new InputAxis(new InputEntry().addKey(Keys.T), new InputEntry().addKey(Keys.Y));
        pixelationControl = new InputAxis(new InputEntry().addKey(Keys.K), new InputEntry().addKey(Keys.L));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        prog = UMath.clamp(prog + progControl.get() * delta * 2, 0, 1);
        fade = UMath.clamp(fade + fadeControl.get() * delta * 2, 0, 1);
        sceneTransitionEffect.setProgress(prog);
        sceneTransitionEffect.setFadePercent(fade);

        pixelation = UMath.clamp(pixelation + pixelationControl.get() * delta * 32, 0, 16);
        pixelationEffect.putParameter("u_pixelation", (int) pixelation);
        pixelationEffect.setActive(pixelation >= 1);

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
        return 320;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void dispose() {
        super.dispose();
        be.dispose();
    }
}
