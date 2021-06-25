package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.rendering.ParameterizedShaderEffect;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class GameScene extends Scene {

    private Player player1;
    private Player player2;
    private ParameterizedShaderEffect pixelationEffect;
    private float pixelation;
    private Ball ball;

    private GameBackground bg;

    @Override
    public void start() {
        super.start();
        add(bg = new GameBackground(new Vector2(0, 0)));
        add(player1 = new Player(new Vector2(getWidth() - 100 - 16, 100), new PlayerControls(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.PERIOD, Input.Keys.SLASH)));
//        add(player2 = new Player(new Vector2(100, 100), new PlayerControls(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S, Input.Keys.F, Input.Keys.G)));

        add(ball = new Ball(new Vector2(getWidth() / 2f, getHeight() / 2f)));

        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, getWidth(), 24)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, 80, 70)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, 80, 70)));
        add(new Block(new Vector2(getWidth() - 80, 0), new Hitbox(0, 0, 80, 70)));
        add(new Block(new Vector2(0, 140), new Hitbox(0, 0, 54, 500)));
        add(new Block(new Vector2(getWidth() - 54, 140), new Hitbox(0, 0, 54, 500)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, 1, 500)));
        add(new Block(new Vector2(getWidth() - 1, 0), new Hitbox(0, 0, 1, 500)));

        sceneTransitionEffect.setTransitionTexture(Assets.get().getCommon().getTransitionTexture("pokemon.png"));
        sceneTransitionEffect.getTransitionColor().set(Color.WHITE);
        rPipeline.addEffect(sceneTransitionEffect);
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
        return "game";
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
