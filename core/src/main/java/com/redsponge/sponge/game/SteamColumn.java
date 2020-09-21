package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PTrigger;

public class SteamColumn extends PTrigger {

    public SteamColumn(Vector2 pos, int width, int height) {
        super(pos);
        getHitbox().set(0, 0, width, height);
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
