package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class CameraManager extends Entity {

    private Player player;
    private Camera camera;

    public CameraManager() {
        super(new Vector2(0,0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        player = scene.first(Player.class);

        camera = scene.getViewport().getCamera();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float lastCamPos = camera.position.y;
//        camera.position.y = UMath.clamp(camera.position.y, player.getPositionf().y - 200, player.getPositionf().y + 100);
//        if(camera.position.y == lastCamPos) {
//            camera.position.y -= 10 * delta;
//        }
//        if(player.getPlayerComponent().isGliding()) {
//            camera.position.y -= 100 * delta;
//        }
    }
}
