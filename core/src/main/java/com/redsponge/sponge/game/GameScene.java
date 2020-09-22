package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

import java.util.ArrayList;

public class GameScene extends Scene {

    private StaticBackground bg;
    private PActor pl;

    public void toggleWorld() {

        mode = mode == WorldMode.FIRE ? WorldMode.ICE : WorldMode.FIRE;
        buildWorld();
    }

    private void buildWorld() {
        for (SteamColumn steamColumn : all(new ArrayList<>(), SteamColumn.class)) {
            remove(steamColumn);
        }
        for (IceBlock iceBlock : all(new ArrayList<>(), IceBlock.class)) {
            remove(iceBlock);
        }
        if(pl != null) remove(pl);
        Vector2 pos = new Vector2(100, 100);
        initWorld();
        pl = mode == WorldMode.FIRE ? new FirePlayer(pos) : new IcePlayer(pos);
        add(pl);
    }

    private void initWorld() {
        if(mode == WorldMode.FIRE) {
            add(new IceBlock(new Vector2(301, 21), new Hitbox(0, 0, 16, 64), false));
            add(new IceBlock(new Vector2(300, 21+64), new Hitbox(0, 0, 64, 16), true));
        } else {
            add(new SteamColumn(new Vector2(200, 16), 128, 160));
        }
    }

    public PActor getPlayer() {
        return pl;
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
        add(new Block(new Vector2(0, 0), new Hitbox(0, 0, getWidth(), 20)));
        add(new Block(new Vector2(-2, 0), new Hitbox(0, 0, 1, 1000)));
        add(new Block(new Vector2(getWidth() + 1, 0), new Hitbox(0, 0, 1, 1000)));
        add(new JumpThru(new Vector2(100, 50), 100));
        add(new SteamColumn(new Vector2(200, 0), 128, 120));
        add(new IceBlock(new Vector2(150, 150), new Hitbox(0, 0, 67, 81), false));

        buildWorld();
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
