package com.redsponge.sponge.physics;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PTrigger extends WorldGeometry {

    public PTrigger(Vector2 pos) {
        super(pos);
    }

    @Override
    public void moveExactX(int amount) {
        setX(getX() + amount);
    }

    @Override
    public void moveExactY(int amount) {
        setY(getY() + amount);
    }

    @Override
    public List<PActor> getRiders(List<PActor> into) {
        return null;
    }
}
