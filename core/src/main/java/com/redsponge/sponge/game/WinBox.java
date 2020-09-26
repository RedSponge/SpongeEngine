package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.PTrigger;

public class WinBox extends PTrigger {

    public WinBox(Vector2 pos, float width, float height) {
        super(pos);
        getHitbox().set(0, 0, (int) width, (int) height);
    }


}
