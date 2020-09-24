package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.PTrigger;

public class DeathBox extends PTrigger implements Killer {

    public DeathBox(Vector2 pos, int width, int height) {
        super(pos);
        getHitbox().set(0, 0, width, height);
    }
}
