package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class CameraManager extends Entity {

    private Player player;
    private Camera camera;
    private float shakeTime;
    private Vector2 requiredPos;

    public CameraManager() {
        super(new Vector2(0,0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        player = scene.first(Player.class);

        camera = scene.getViewport().getCamera();

        EventBus.getInstance().registerListener(this);

        requiredPos = new Vector2(scene.getWidth() / 2f, scene.getHeight() / 2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        camera.position.set(requiredPos, 0);

        if(shakeTime > 0) {
            camera.position.x += MathUtils.random(-5, 5);
            camera.position.y += MathUtils.random90(-5, 5);
            shakeTime -= delta;
        }
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    @EventHandler
    public void onShake(CameraShakeEvent event) {
        shakeTime += event.time;
    }
}
