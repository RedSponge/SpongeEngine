package com.redsponge.sponge.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.assets.Assets;
import com.redsponge.sponge.game.GameScene;
import com.redsponge.sponge.screen.Scene;

public class MenuScene extends Scene {

    private boolean startingGame;
    private float startGameTime;

    @Override
    public void start() {
        super.start();
        add(new MenuBackground());
        add(new GlowingTexture(new Vector2(55 * 4, 36 * 4), "grow_cake.png"));
        add(new GlowingTexture(new Vector2(95 * 4, 36 * 4), "shrink_bottle.png"));

        add(new Prompt(new Vector2(getWidth() / 2f - 45, 40)));

        sceneTransitionEffect.setTransitionTexture(Assets.get().getCommon().getTransitionTexture("slide_up.png"));
        getRenderingPipeline().addEffect(sceneTransitionEffect);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(startingGame) {
            startGameTime += delta;
            sceneTransitionEffect.setProgress(startGameTime / 0.1f);
            if(startGameTime > 0.1f) {
                SpongeGame.i().setNextScene(new GameScene());
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                startingGame = true;
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 255 / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public int getWidth() {
        return 240*2;
    }

    @Override
    public int getHeight() {
        return 320*2;
    }

    @Override
    public String getName() {
        return "menu";
    }
}
