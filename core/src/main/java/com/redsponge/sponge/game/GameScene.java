package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.game.win.WinScene;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.screen.Scene;

import java.applet.Applet;

public class GameScene extends Scene {

    private boolean isRunning;
    private float timeSinceDeath;
    private float deathTransitionTime = 0.2f;
    private float timeRunning;
    private float transitionTimeRunning = 0.2f;
    private BloomEffect bloomEffect;
    private boolean won;
    private Sound completedSound;

    @Override
    public void start() {
        isRunning = true;
        super.start();
        add(new StaticBackground());
        add(new Player());
        add(new BackgroundRenderer());

        add(new CameraManager());
        add(new GameTimeManager());
        add(new GameBar(new Vector2(getWidth() - 100, 20)));

        add(new Boundary(-1, 0, 1, getHeight()));
        add(new Boundary(getWidth(), 0, 1, getHeight()));

        add(new PowerupSpawner());
        add(new ObstacleSpawner());

        bloomEffect = new BloomEffect(true, SpongeGame.i().getBatch(), getRenderingPipeline().getGameViewport(), 2);
        getRenderingPipeline().addEffect(bloomEffect);

        getRenderingPipeline().addEffect(sceneTransitionEffect);
        sceneTransitionEffect.setTransitionTexture(getAssets().get("slide_down_transition.png"));
        EventBus.getInstance().registerListener(this);

        completedSound = assets.get("win.ogg");
    }

    public BloomEffect getBloomEffect() {
        return bloomEffect;
    }

    @Override
    public void update(float delta) {
        timeRunning = SpongeGame.i().getElapsedTime() - getStartTime();

        if(isRunning) {
            sceneTransitionEffect.setProgress(Interpolation.circleOut.apply(Math.max(1 - timeRunning / transitionTimeRunning, 0)));
            super.update(delta);
        } else {
            timeSinceDeath += delta;
            sceneTransitionEffect.setProgress(Interpolation.circleIn.apply(timeSinceDeath / deathTransitionTime));
            if(timeSinceDeath >= deathTransitionTime) {
                if(won) {
                    SpongeGame.i().setNextScene(new WinScene());
                } else {
                    SpongeGame.i().setNextScene(new GameScene());
                }
             }
        }
    }

    @Override
    public void dispose() {
        EventBus.getInstance().removeListener(this);
        super.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1 / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @EventHandler
    public void onPlayerDeath(Player.PlayerDeathEvent event) {
        isRunning = false;
        sceneTransitionEffect.setTransitionTexture(getAssets().get("slide_up_transition.png"));
    }

    @EventHandler
    public void onCompleted(CompletedEvent event) {
        isRunning = false;
        completedSound.play(0.05f);
        sceneTransitionEffect.setTransitionTexture(getAssets().get("slide_up_transition.png"));
        won = true;
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
