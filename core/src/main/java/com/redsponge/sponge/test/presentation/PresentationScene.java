package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.test.Player;
import com.redsponge.sponge.test.StaticBackground;

public class PresentationScene extends Scene {

    public PresentationScene() {
        Player p;
        add(new StaticBackground());
        add(new MapEntity());
        add(p = new Player(new Vector2(100, 300)));
        add(new Enemy(new Vector2(400, 400)));
        add(new CameraHandler(p));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public int getWidth() {
        return 320*2;
    }

    @Override
    public int getHeight() {
        return 180*2;
    }

    @Override
    public String getName() {
        return "test";
    }
}
