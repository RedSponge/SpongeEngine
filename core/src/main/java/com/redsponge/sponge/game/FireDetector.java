package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FireDetector extends PActor {

    private int sign = 1;
    private boolean done;
    private Set<IceBlock> blocks;

    public FireDetector(Vector2 pos, int width, int height) {
        super(pos);
        getHitbox().set(0, 0, width, height);
        blocks = new HashSet<>();
    }

    @Override
    public void update(float delta) {
        for (IceBlock iceBlock : all(new ArrayList<>(), IceBlock.class)) {
            if(check(iceBlock)) {
                iceBlock.reMelt();
            }
        }
        removeSelf();
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().setColor(Color.ORANGE);
        SpongeGame.i().getShapeDrawer().rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
