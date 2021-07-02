package com.redsponge.sponge.physics;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.CollisionEvent;
import com.redsponge.sponge.event.EventBus;

public class PActor extends Entity {

    private Vector2 remainder;
    private Vector2 movedByGeometry; // How much was moved by world geometry last frame

    private Vector2 tmp;

    public PActor(Vector2 pos) {
        super(pos);
        remainder = new Vector2();
        movedByGeometry = new Vector2();
        tmp = new Vector2();
    }

    public boolean groundCheck() {
        return groundCheck(1);
    }

    public boolean groundCheck(int distance) {
        return check(PSolid.class, tmp.set(0, -distance)) || checkOutside(JumpThru.class, tmp.set(0, -distance));
    }

    public boolean isRiding(PSolid solid) {
        return check(solid, tmp.set(0, -1));
    }

    public boolean isRiding(JumpThru jumpThru) {
        return checkOutside(jumpThru, tmp.set(0, -1));
    }

    public void squish(Collision collision) {
        removeSelf();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        movedByGeometry.set(Vector2.Zero);
    }

    public boolean moveX(float amount, CollisionHandler onCollision) {
        remainder.x += amount;
        int move = Math.round(remainder.x);

        if(move != 0) {
            remainder.x -= move;
            return moveExactX(move, onCollision, null, null);
        }
        return false;
    }

    public boolean moveExactX(int amount, CollisionHandler onCollision, WorldGeometry pusher, WorldGeometry carrier) {
        int move = amount;
        int sign = (int) Math.signum(move);
        boolean byGeometry = carrier != null || pusher != null;

        while(move != 0) {
            PSolid hit = first(PSolid.class, tmp.set(sign, 0));
            if(hit != null) {
                Collision c = new Collision(tmp.set(sign, 0), Math.abs(amount), Math.abs(amount - move), hit, pusher);
                if(onCollision != null) onCollision.onCollision(c);
                EventBus.getInstance().dispatch(new CollisionEvent(c));

                return true;
            }

            setX(getX() + sign);
            if(byGeometry) {
                this.movedByGeometry.x += sign;
            }
            move -= sign;
        }

        return false;
    }

    public boolean moveY(float amount, CollisionHandler onCollision) {
        remainder.y += amount;
        int move = Math.round(remainder.y);

        if(move != 0) {
            remainder.y -= move;
            return moveExactY(move, onCollision, null, null);
        }
        return false;
    }

    public void moveToX(float x) {
        moveX(x - (getX() + remainder.x), null);
    }

    public void moveToY(float y) {
        moveY(y - (getY() + remainder.y), null);
    }


    public boolean moveExactY(int amount, CollisionHandler onCollision, WorldGeometry pusher, WorldGeometry carrier) {
        int move = amount;
        int sign = (int) Math.signum(amount);

        boolean byGeometry = pusher != null || carrier != null;

        while(move != 0) {
            WorldGeometry hit = first(PSolid.class, tmp.set(0, sign));
            if(hit == null && sign == -1) {
                hit = firstOutside(JumpThru.class, tmp.set(0, sign));
            }

            if(hit != null) {
                Collision c = new Collision(tmp.set(0, sign), Math.abs(amount), Math.abs(amount - move), hit, pusher);
                if(onCollision != null) onCollision.onCollision(c);
                EventBus.getInstance().dispatch(new CollisionEvent(c));
                return true;
            }

            setY(getY() + sign);
            if(byGeometry) {
                movedByGeometry.y += sign;
            }
            move -= sign;
        }

        return false;
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

    public Vector2 getMovedByGeometry() {
        return movedByGeometry;
    }
}
