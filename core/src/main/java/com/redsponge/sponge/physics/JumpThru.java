package com.redsponge.sponge.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;

import java.util.ArrayList;
import java.util.List;

public class JumpThru extends WorldGeometry {

    public JumpThru(Vector2 pos, int width) {
        super(pos);
        getHitbox().set(0, 0, width, 2);
    }

    @Override
    public void moveExactX(int amount) {
        if(isCollidable()) {
            List<PActor> riders = getRiders(new ArrayList<>());
            getPosition().x += amount;
            for (PActor rider : riders) {
                rider.moveExactX(amount, null, null, this);
            }
        } else {
            getPosition().x += amount;
        }
    }

    @Override
    public void moveExactY(int amount) {
        if(isCollidable()) {
            List<PActor> riders = getRiders(new ArrayList<>());

            if(amount > 0) {
                for (PActor actor : getScene().all(new ArrayList<>(), PActor.class)) {
                    if(riders.contains(actor) || checkOutside(actor, Vector2.Y.cpy().scl(amount))) {
                        int move = (getTop() + amount) - actor.getBottom();
                        actor.moveExactY(move, null, null, this);
                    }
                }
            } else {
                setCollidable(false);
                for (PActor rider : riders) {
                    rider.moveExactY(amount, null, null, this);
                }
                setCollidable(true);
            }
        }
        getPosition().y += amount;
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
        super.render();
        SpongeGame.i().getShapeDrawer().line(getPositionf().x, getPositionf().y, getPositionf().x + getWidth(), getPositionf().y, Color.YELLOW);
    }
}
