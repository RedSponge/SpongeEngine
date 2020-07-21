package com.redsponge.sponge.physics;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.util.Hitbox;

import java.util.ArrayList;
import java.util.List;

public class PSolid extends WorldGeometry {

    public PSolid(Vector2 pos, Hitbox hitbox) {
        super(pos);
        getHitbox().set(hitbox);
    }

    @Override
    public void moveExactX(int amount) {
        if(isCollidable()) {
            List<PActor> riders = getRiders(new ArrayList<>());
            setX(getX() + amount);
            setCollidable(false);

            for (PActor actor : getScene().all(new ArrayList<>(), PActor.class)) {
                if(check(actor)) {
                    int move;
                    if(amount > 0) {
                        move = getRight() - actor.getLeft();
                    } else {
                        move = getLeft() - actor.getRight();
                    }

                    actor.moveExactX(move, actor::squish, this, null);
                } else if(riders.contains(actor)) {
                    actor.moveExactX(amount, null, null, this);
                }
            }
            setCollidable(true);
        }
        else {
            setX(getX() + amount);
        }
    }

    @Override
    public void moveExactY(int amount) {
        if(isCollidable()) {
            List<PActor> riders = getRiders(new ArrayList<>());
            setY(getY() + amount);
            setCollidable(false);

            for (PActor actor : getScene().all(new ArrayList<>(), PActor.class)) {
                if(check(actor)) {
                    int move;
                    if(amount > 0) {
                        move = getTop() - actor.getBottom();
                    } else {
                        move = getBottom() - actor.getTop();
                    }
                    actor.moveExactY(move, actor::squish, this, null);
                } else if(riders.contains(actor)) {
                    actor.moveExactY(amount, null, null, this);
                }
            }

            setCollidable(true);
        }
        else {
            setY(getY() + amount);
        }
    }

    @Override
    public List<PActor> getRiders(List<PActor> into) {
        for (PActor actor : getScene().all(new ArrayList<>(), PActor.class)) {
            if(actor.isRiding(this)) {
                into.add(actor);
            }
        }
        return into;
    }

    @Override
    public void render() {
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }
}
