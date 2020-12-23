package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.physics.PActor;

public class Enemy extends PActor {

    private AnimationComponent drawn;
    private AnimationNodeSystem system;

    public Enemy(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 16, 30);
    }


}
