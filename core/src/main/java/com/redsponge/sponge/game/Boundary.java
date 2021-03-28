package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.util.Hitbox;

public class Boundary extends PSolid {

    public Boundary(int x, int y, int w, int h) {
        super(new Vector2(x, y), new Hitbox(0, 0, w, h));
    }

    @Override
    public void render() {
//        super.render();
    }
}
