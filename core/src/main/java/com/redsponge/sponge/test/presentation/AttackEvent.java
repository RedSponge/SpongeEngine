package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.math.Rectangle;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.test.Attack;

public class AttackEvent {

    public Entity caller;
    public Attack attack;

    public AttackEvent(Entity caller, Attack attack) {
        this.caller = caller;
        this.attack = attack;
    }
}
