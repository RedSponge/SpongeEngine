package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;

public class GameScene extends Scene {

    private StaticBackground bg;
    private PActor pl;

    private MapManager mm;
    private CameraManager cm;
    private boolean restartRequired;
    private boolean won;
    private float winTime;
    private static final float maxWinTime = 0.5f;

    private ScalingViewport sv;

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

    public void win() {
        won = true;
        winTime = 0;
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
        add(mm = new MapManager());
        add(cm = new CameraManager());

        mm.load("game/map/ice_test.tmx");
        sv = new ScalingViewport(Scaling.fill, 1, 1);
    }

    @Override
    public void update(float delta) {
        if(restartRequired) {
            mm.load("game/map/ice_test.tmx");
            cm.setMM(mm);
            restartRequired = false;
        }
        AnimatedTiledMapTile.updateAnimationBaseTime();
        Connections.prepCache();
        super.update(delta);
        if(won) {
            winTime += delta;
            if(winTime > maxWinTime) {
                SpongeGame.i().setScene(new WinScene());
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        mm.renderForeground();
        SpongeGame.i().getBatch().end();

        sv.apply();
        SpongeGame.i().getBatch().begin();
        SpongeGame.i().getBatch().setProjectionMatrix(sv.getCamera().combined);
        SpongeGame.i().getShapeDrawer().filledRectangle(0, 0, 1, 1, new Color(0, 0, 0, winTime / maxWinTime));
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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        sv.update(width, height, true);
    }
}
