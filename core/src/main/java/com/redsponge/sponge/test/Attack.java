package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Attack {

    public Vector2 dir;
    public Rectangle attackRect;

    public Attack(Vector2 dir, Rectangle attackRect) {
        this.dir = dir;
        this.attackRect = attackRect;
    }
}
