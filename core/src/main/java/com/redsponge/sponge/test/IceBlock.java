package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.util.Hitbox;

public class IceBlock extends PSolid {

    private float time;
    private static final float upTime = .2f;
    private static final float maxTime = 2;

    public IceBlock(float x, int height) {
        super(new Vector2(x - 16 + 4, -height), new Hitbox(0, 0, 32, height));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
        float currentHeight = Interpolation.fastSlow.apply(Math.min(time / upTime, 1)) * getHitbox().getHeight() - getHitbox().getHeight();
        moveToY(currentHeight);

        if(time > maxTime) {
            removeSelf();
        }
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().setColor(Color.BLUE);
        SpongeGame.i().getShapeDrawer().filledRectangle(getX(), getY(), getWidth(), getHeight());
    }
}
