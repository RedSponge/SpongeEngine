package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.screen.Scene;

public class GameScene extends Scene {

    @Override
    public void start() {
        super.start();
        add(new StaticBackground());
        add(new Player());
        add(new BackgroundRenderer());

        add(new CameraManager());
        add(new GameTimeManager());
        add(new GameBar(new Vector2(getWidth() - 100, 20)));

        add(new GrowCake(new Vector2(200, 200)));
        add(new ShrinkBottle(new Vector2(100, 100)));
        add(new Boundary(-1, 0, 1, getHeight()));
        add(new Boundary(getWidth(), 0, 1, getHeight()));

        add(new ObstacleSpawner());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1 / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public int getWidth() {
        return (int) (480);
    }

    @Override
    public int getHeight() {
        return (int) (640);
    }

    @Override
    public String getName() {
        return "game";
    }

}
