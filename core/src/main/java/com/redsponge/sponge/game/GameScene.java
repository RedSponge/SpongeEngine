package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.game.light.Lighting;
import com.redsponge.sponge.game.light.PointLight;
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

    private Lighting lighting;

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

    PointLight mouseLight;

    public Lighting getLighting() {
        return lighting;
    }

    enum WorldMode {
        FIRE("game/map/fire_test.tmx"),
        ICE("game/map/ice_test.tmx")

        ;

        String file;

        WorldMode(String file) {
            this.file = file;
        }
    }

    private WorldMode mode;

    @Override
    public void start() {
        super.start();
        mouseLight = new PointLight(0, 0, 32, 32, assets.<TextureAtlas>get("lights.atlas").findRegion("point/feathered"));
        lighting = new Lighting(viewport);
        lighting.addLight(mouseLight);
        mode = WorldMode.ICE;

        add(bg = new StaticBackground());
        add(mm = new MapManager());
        add(cm = new CameraManager());

        mm.load(mode.file);
        sv = new ScalingViewport(Scaling.fill, 1, 1);
    }

    @Override
    public void update(float delta) {
        if(restartRequired) {
            mm.load(mode.file);
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

        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        mouseLight.setX(mousePos.x);
        mouseLight.setY(mousePos.y);

//        lighting.drawLights();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        mm.renderForeground();
        SpongeGame.i().getBatch().end();

//        lighting.drawOnScreen();

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
        lighting.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        lighting.dispose();
    }
}
