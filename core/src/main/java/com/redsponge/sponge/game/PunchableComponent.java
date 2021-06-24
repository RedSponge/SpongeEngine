package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.entity.Component;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;

public class PunchableComponent extends Component {

    private Vector2 vel;
    private TimedAction punchTimer;

    public PunchableComponent(boolean active, boolean visible, Vector2 entityVelocity) {
        super(active, visible);
        this.vel = entityVelocity;
        punchTimer = new TimedAction();
    }

    @Override
    public void begin() {
        EventBus.getInstance().registerListener(this);
    }

    @Override
    public void update(float delta) {
        punchTimer.update(delta);
    }

    @Override
    public void render() {
        SpongeGame.i().getShapeDrawer().setColor(1, 0, 0, 0.5f);
        getEntity().drawHitbox(SpongeGame.i().getShapeDrawer());
        SpongeGame.i().getShapeDrawer().setColor(1, 1, 1, 1);
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    @EventHandler
    public void onPunch(PunchEvent event) {
        if(!isActive()) return;
        if(event.getPuncher() == getEntity()) return;

        if(getEntity().getSceneHitbox().intersects(event.getPunchBox())) {
            Vector2 diff = getEntity().getSceneHitbox().getCenter().sub(event.getPunchBox().getCenter()).nor();
            Vector2 genDir = event.getGeneralDirection();

            float diffMult = 1 / 5f;
            float genMult = 1 - diffMult;

            Vector2 merged = diff.scl(diffMult).mulAdd(genDir, genMult).nor();

            vel.set(merged).scl(300);
            punchTimer.setValue(0.2f);
        }
    }

    public boolean shouldDecay() {
        return !punchTimer.isActive();
    }
}
