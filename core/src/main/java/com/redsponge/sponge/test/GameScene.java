package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.screen.Scene;

public class GameScene extends Scene {

    private LevelSimulator simulator;

    @Override
    public void start() {
        super.start();
        add(new StaticBackground());
        simulator = LevelParser.parseLevel(Gdx.files.internal("level.txt").readString());//new LevelSimulator();
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(1, 0)), 0, 0);
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(0, 1)), 4, 0);
//        simulator.setRoomObject(new SkipTile(), 4, 2);
//        simulator.setRoomObject(new SkipTile(), 4, 4);
//        simulator.setRoomObject(new OrbGiveTile(), 4, 6);
//        simulator.setRoomObject(new DeathTile(), 4, 5);
//        addPortalPair(4, 3, 6, 4);
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(-1, 0)), 6, 6);
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(0, -1)), 0, 6);
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(1, 0)), 0, 4);
//        simulator.setRoomObject(new DirectionSetTile(new Vector2(0, -1)), 6, 3);
//        addPortalPair(6, 0, 0, 2);

//        add(new TestRoomRenderer(simulator));
        add(new IsometricTileMapRenderer(new Vector2(100,0), simulator));
        add(new IsometricPlayerRenderer(new Vector2(), simulator.getPlayerPos(), simulator.getPlayerVel()));
    }

    private void addPortalPair(int x1, int y1, int x2, int y2) {
//        PortalTile a = new PortalTile(colour);
//        PortalTile b = new PortalTile(a, colour);
//        a.setOther(b);
//        simulator.setRoomObject(a, x1, y1);
//        simulator.setRoomObject(b, x2, y2);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            simulator.progressPlayer();
        }
        super.update(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public int getWidth() {
        return 320;
    }

    @Override
    public int getHeight() {
        return 180;
    }

    @Override
    public String getName() {
        return "game";
    }
}
