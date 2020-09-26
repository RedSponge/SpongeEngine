package com.redsponge.sponge.game.outro;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PTrigger;

public class StartTalkingTrigger extends PTrigger {
    public StartTalkingTrigger(Vector2 pos, float width, float height) {
        super(pos);
        getHitbox().set(0, 0, (int) width, (int) height);
    }

    @Override
    public void render() {
        super.render();
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }
}
