package com.redsponge.sponge.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;

public class PowerupSpawner extends Entity {

    private float time;

    public PowerupSpawner() {
        super(new Vector2());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
        if(time > 6) {
            time -= 6;
            spawnPowerup();
        }
    }

    private void spawnPowerup() {
        if(MathUtils.randomBoolean()) {
            getScene().add(new GrowCake(new Vector2(MathUtils.random(100, getScene().getWidth() - 100), -60)));
        } else {
            getScene().add(new ShrinkBottle(new Vector2(MathUtils.random(100, getScene().getWidth() - 100), -60)));
        }
    }
}
