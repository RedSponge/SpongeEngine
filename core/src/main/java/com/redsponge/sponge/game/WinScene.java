package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.redsponge.sponge.screen.Scene;

public class WinScene extends Scene {

    private MapManager mm;

    @Override
    public void start() {
        super.start();
        add(mm = new MapManager());
        mm.load("game/map/outro.tmx");
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        AnimatedTiledMapTile.updateAnimationBaseTime();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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

    @Override
    public String getName() {
        return "win";
    }

    public void swap() {
        swapTime = 0;
        isSwapping = true;
    }
}
