package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;

public class GameScene extends Scene {

    private StaticBackground bg;
    private PActor pl;

    private MapManager mm;
    private CameraManager cm;
    private boolean restartRequired;

    public void toggleWorld() {

        mode = mode == WorldMode.FIRE ? WorldMode.ICE : WorldMode.FIRE;
        buildWorld();
    }

    private void buildWorld() {

    }

    private void initWorld() {

    }

    public PActor getPlayer() {
        return pl;
    }

    public void setPlayer(PActor pl) {
        this.pl = pl;
    }

    public MapManager getMapManager() {
        return mm;
    }

    public CameraManager getCameraManager() {
        return cm;
    }

    public void restartLevel() {
        restartRequired = true;
    }

    enum WorldMode {
        FIRE,
        ICE
    }

    private WorldMode mode;

    @Override
    public void start() {
        super.start();
        mode = WorldMode.FIRE;

        add(bg = new StaticBackground());
        add(mm = new MapManager());
        add(cm = new CameraManager());

        mm.load("game/map/fire_test.tmx");

        buildWorld();
    }

    @Override
    public void update(float delta) {
        if(restartRequired) {
            mm.load("game/map/fire_test.tmx");
            cm.setMM(mm);
            restartRequired = false;
        }
        AnimatedTiledMapTile.updateAnimationBaseTime();
        Connections.prepCache();
        super.update(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        mm.renderForeground();
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
