package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class Ball extends PActor {

    private DrawnComponent drawn;
    private PunchableComponent punchable;
    private Vector2 vel;

    public Ball(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 20, 20);
        setzIndex(6);
        vel = new Vector2();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, getScene().getAssets().<TextureAtlas>get("game.atlas").findRegion("ball"));
        add(drawn);
        drawn.setOffsetX(-6).setOffsetY(-6);
        drawn.setOriginX(16).setOriginY(16);
        punchable = new PunchableComponent(true, true, vel, 300);
        add(punchable);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        vel.y = UMath.approach(vel.y, -300, 200 * delta);
        boolean isOnGround = groundCheck();
        if(isOnGround) {
            vel.x *= .99f;
        }

        if(vel.len2() > 300 * 300 && punchable.shouldDecay()) {
            vel.scl(0.9f);
        }


        moveX(vel.x * delta, this::onCollideX);
        moveY(vel.y * delta, this::onCollideY);

        if(Math.abs(vel.x) < .5f) {
            vel.x = 0;
        }

        if(Math.abs(vel.y) < 1) {
            vel.y = 0;
        }

        drawn.setRotation(drawn.getRotation() - vel.x * delta * vel.len2() / 10000);
    }

    private void onCollideY(Collision collision) {
        vel.y *= -.8f;
        zeroRemainderY();
    }

    private void onCollideX(Collision collision) {
        vel.x *= -.8f;
        zeroRemainderX();
    }

    @Override
    public void render() {
        super.render();
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }
}
