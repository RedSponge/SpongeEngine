package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.screen.Scene;

public class GameScene extends Scene {

    private LevelSimulator simulator;
    private GuiRenderer guiRenderer;

    private Level level;

    private int currentLevel = 8;
    private String[] levels = new String[] {
            "levels/l1.txt",
            "levels/l2.txt",
            "levels/l3.txt",
            "levels/l4.txt",
            "levels/l5.txt",
            "levels/l6.txt",
            "levels/middle.txt",
            "levels/two_portals.txt",
            "levels/impossible.txt"
    };
    private float winTime;
    private Music music;


    @Override
    public void start() {
        super.start();
        EventBus.getInstance().registerListener(this);

        add(new StaticBackground());
        simulator = new LevelSimulator();

        rPipeline.addEffect(new BloomEffect(true, SpongeGame.i().getBatch(), getViewport(), 2));

        add(new IsometricTileMapRenderer(new Vector2(getViewport().getWorldWidth() / 2f - 19,0), simulator));
        add(new IsometricPlayerRenderer(new Vector2(), simulator.getPlayerPos(), simulator.getPlayerVel()));
        add(guiRenderer = new GuiRenderer());

        loadLevel(levels[currentLevel]);

        rPipeline.addEffect(sceneTransitionEffect);
        music = Gdx.audio.newMusic(Gdx.files.internal("restless.ogg"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    private void loadLevel(String path) {
        level = LevelParser.parseLevel(Gdx.files.internal(path).readString());
        simulator.loadLevel(level.getTiles());
        guiRenderer.loadLevel(level);
        simulator.reset();
        guiRenderer.onDie(null);
    }

    private void addPortalPair(int x1, int y1, int x2, int y2) {
//        PortalTile a = new PortalTile(colour);
//        PortalTile b = new PortalTile(a, colour);
//        a.setOther(b);
//        simulator.setRoomObject(a, x1, y1);
//        simulator.setRoomObject(b, x2, y2);
    }

    @EventHandler
    public void onWin(WinEvent event) {
        winTime = 2;
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            simulator.progressPlayer();
        }
        if(winTime > 1 && winTime - delta <= 1) {
            loadLevel(levels[++currentLevel]);
        }
        winTime -= delta;
        sceneTransitionEffect.setFadePercent(winTime > 1 ? (2 - winTime) : winTime);
        super.update(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @EventHandler
    public void onResetLevel(ResetMoveEvent event) {
        loadLevel(levels[currentLevel]);
//        loadLevel("level.txt");
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

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
    }
}