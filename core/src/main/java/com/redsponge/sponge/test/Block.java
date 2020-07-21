package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.util.Hitbox;

public class Block extends PSolid {

    public Block(Vector2 pos, Hitbox hitbox) {
        super(pos, hitbox);
        setCollidable(true);
    }
}
