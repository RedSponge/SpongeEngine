package com.redsponge.sponge.physics;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;

import java.util.List;

public abstract class WorldGeometry extends Entity {

    private Vector2 remainder;

    public WorldGeometry(Vector2 pos) {
        super(pos);
    }

    public void moveX(float amount) {
        remainder.x += amount;
        int moveBy = Math.round(remainder.x);
        if(moveBy != 0) {
            remainder.x -= moveBy;
            moveExactX(moveBy);
        }
    }

    public void moveY(float amount) {
        remainder.y += amount;
        int moveBy = Math.round(remainder.y);
        if(moveBy != 0) {
            remainder.y -= moveBy;
            moveExactY(moveBy);
        }
    }

    public void move(Vector2 amount) {
        moveX(amount.x);
        moveY(amount.y);
    }

    public void moveToX(float x) {
        moveX(x - (getX() + remainder.x));
    }

    public void moveToY(float y) {
        moveY(y - (getY() + remainder.y));
    }

    public void moveTo(Vector2 target) {
        moveToX(target.x);
        moveToY(target.y);
    }

    public void zeroRemainderX() {
        remainder.x = 0;
    }

    public void zeroRemainderY() {
        remainder.y = 0;
    }

    public void zeroRemainder() {
        remainder.set(Vector2.Zero);
    }

    public abstract void moveExactX(int amount);
    public abstract void moveExactY(int amount);
    public abstract List<PActor> getRiders(List<PActor> into);
}
