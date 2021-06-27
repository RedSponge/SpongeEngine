package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;

public class GameTimeManager extends Entity {

    private float time = 0;
    private float maxTime = 90;

    public GameTimeManager() {
        super(new Vector2());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
        if(time >= maxTime) {
            time = maxTime;
            EventBus.getInstance().dispatch(new CompletedEvent());
        }
    }

    public float getTime() {
        return time;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public float getCompletedPercent() {
        return time / maxTime;
    }
}
