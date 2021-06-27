package com.redsponge.sponge.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

import java.applet.Applet;

public class Obstacle extends Entity {

    private final String obstacleTexture;
    private DrawnComponent drawn;
    private float time;
    private float rotSpeed;
    private Vector2 direction;
    private float minSpeed = -100;
    private float maxSpeed = -180;
    private Sound shatterSound;
    private String shatterSoundFile;
    private boolean shattered;

    private float intensity;

    public Obstacle(Vector2 pos, String obstacleTexture, float intensity) {
        super(pos);
        this.obstacleTexture = obstacleTexture;
        this.shatterSoundFile = Constants.OBSTACLE_SOUNDS.get(obstacleTexture);
        this.intensity = intensity;
        System.out.println(shatterSoundFile);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion(obstacleTexture));
        add(drawn);

        getHitbox().set(6, 6, (int)drawn.getCalculatedWidth() * 2 - 6, (int)drawn.getCalculatedHeight() * 2 - 6);
        drawn.setScaleY(2).setScaleX(2);
        drawn.setOriginX(drawn.getCalculatedWidth() / 2f).setOriginY(drawn.getCalculatedHeight() / 2f);
        drawn.setOffsetX(drawn.getCalculatedWidth() / 2f).setOffsetY(drawn.getCalculatedWidth() / 2f);

        direction = new Vector2( MathUtils.random(10, 30) * MathUtils.randomSign(), MathUtils.random(minSpeed * intensity, maxSpeed * intensity));
        rotSpeed = direction.y * MathUtils.randomSign();
        shatterSound = scene.getAssets().get(shatterSoundFile);//Gdx.audio.newSound(Gdx.files.internal("game/sound/" + shatterSoundFile));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
        drawn.setRotation(time * rotSpeed);

        getPositionf().mulAdd(direction, delta);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void removed() {
        super.removed();
    }

    public void shatter() {
        shatterSound.play(1.5f);
        removeSelf();
    }
}
