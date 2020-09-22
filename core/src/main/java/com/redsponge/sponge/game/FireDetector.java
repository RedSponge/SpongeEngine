package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;

public class FireDetector extends PActor {

    private int sign = 1;

    public FireDetector(Vector2 pos, int width, int height) {
        super(pos);
        getHitbox().set(0, 0, width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        moveX(sign, this::check);
        sign *= -1;
        removeSelf();
    }

    private void check(Collision collision) {
        if(collision.stopper instanceof IceBlock) {
            ((IceBlock) collision.stopper).reMelt();
        }
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().setColor(Color.ORANGE);
        SpongeGame.i().getShapeDrawer().rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
