package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.util.Hitbox;

public class PunchEvent {

    private final Hitbox punchBox;
    private final Entity puncher;
    private final Vector2 generalDirection;

    public PunchEvent(Hitbox punchBox, Entity puncher, Vector2 generalDirection) {
        this.punchBox = punchBox;
        this.puncher = puncher;
        this.generalDirection = generalDirection.nor();
    }

    public Hitbox getPunchBox() {
        return punchBox;
    }

    public Entity getPuncher() {
        return puncher;
    }

    public Vector2 getGeneralDirection() {
        return generalDirection;
    }
}
