package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class GameScene extends Scene {

    private StaticBackground bg;
    private Player pl;

    public void toggleWorld() {
        mode = mode == WorldMode.FIRE ? WorldMode.ICE : WorldMode.FIRE;
    }

    enum WorldMode {
        FIRE,
        ICE
    }

    private WorldMode mode;

    @Override
    public void start() {
        super.start();
        mode = WorldMode.ICE;
        add(bg = new StaticBackground());
        add(pl = new Player(new Vector2(100, 100)));
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, getWidth(), 20)));
        add(new Block(new Vector2(-2, 0), new Hitbox(0, 0, 1, 1000)));
        add(new Block(new Vector2(getWidth() + 1, 0), new Hitbox(0, 0, 1, 1000)));
        add(new JumpThru(new Vector2(100, 50), 100));
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
    public int getWidth() {
        return 480;
    }

    @Override
    public int getHeight() {
        return 270;
    }

    public WorldMode getMode() {
        return mode;
    }

    @Override
    public String getName() {
        return "game";
    }
}
