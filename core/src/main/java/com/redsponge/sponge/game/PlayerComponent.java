package com.redsponge.sponge.game;

import com.redsponge.sponge.entity.Component;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.CollisionEvent;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.util.UMath;

public class PlayerComponent extends Component {

    private float vx;
    private float vy;

    private final float accelSpeed = 900;
    private final float maxSpeed = 100;
    private final float turnMultiplier = 4f;
    private final float slowDownMultiplier = 0.8f;
    private final float constGlidingMultiplier = -1;

    private boolean isGliding;
    private float glidingMultiplier;

    private final float maxGravity = -300;
    private float gravity = 200;

    public PlayerComponent(boolean active, boolean visible) {
        super(active, visible);
    }

    @Override
    public void begin() {

    }

    @Override
    public void added(Entity entity) {
        super.added(entity);
        EventBus.getInstance().registerListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    @Override
    public void update(float delta) {
        boolean wasGliding = isGliding;
        isGliding = Controls.GLIDE.isPressed();

        updateVX(delta);
        updateVY(delta);
    }

    private void updateVY(float delta) {
        if(isGliding) {
            if(vy < 0) {
                vy += 100 * delta;
            }
            vy += 40 * delta;
        } else {
            vy -= 100 * delta;
        }
        vy = UMath.clamp(vy, maxGravity, 80);
        ((PActor)getEntity()).moveY(vy * delta, null);
    }

    private void updateVX(float delta) {
        int currentVx = Controls.HORIZONTAL.get();
        if(currentVx != 0) {
            float dirChangeMultiplier = Math.signum(currentVx) == Math.signum(vx) ? 1 : turnMultiplier;
            vx += currentVx * delta * accelSpeed * dirChangeMultiplier;
            vx = UMath.clamp(vx, -maxSpeed, maxSpeed);
        } else {
            vx *= slowDownMultiplier;
            if(Math.abs(vx) < 1) vx = 0;
        }
        ((PActor)getEntity()).moveX(vx * delta, null);
    }

    @EventHandler
    public void onCollision(CollisionEvent event) {
        if(event.getCollider() == getEntity()) {
            if(event.getCollision().dir.x != 0) {
                vx = 0;
            }
            if(event.getCollision().dir.y != 0) {
                vy = 0;
            }
        }
    }

    @Override
    public void render() {

    }

    public boolean isGliding() {
        return isGliding;
    }

    public float getVX() {
        return vx;
    }

}
