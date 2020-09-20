package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class Player extends PActor {

    private Vector2 vel;

    private final TimedAction jumpGraceTime;
    private final TimedAction varJumpTime;
    private final TimedAction jumpMemoryTime;
    private final TimedAction forceDownTime;
    private final TimedAction powerTime;

    // region constants
    private static final float coyoteTime = 0.1f;
    private static final float jumpTime = 0.2f;
    private static final float jumpSpeed = 160;
    private static final float jumpHorizontalBoost = 30;
    private static final float maxFall = 160;
    private static final float gravity = -1000;
    private static final float halfGravityThreshold = 40;
    private static final float maxRun = 200;
    private static final float accel = 800;
    private static final float accelAirMultiplier = 0.8f;
    private static final float maxJumpMemoryTime = 0.1f;
    // endregion

    private boolean onGround;
    private DrawnComponent drawn;

    public Player(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 16, 16);
        add(jumpGraceTime = new TimedAction());
        add(varJumpTime = new TimedAction());
        add(jumpMemoryTime = new TimedAction());
        add(forceDownTime = new TimedAction());
        add(powerTime = new TimedAction());
        vel = new Vector2();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        add(drawn = new DrawnComponent(true, true, getScene().getAssets().<Texture>get("player.png")));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        onGround = groundCheck();

        if(powerTime.isRunning()) {

        } else {
            updateVY(delta);
            updateVX(delta);

            if(!Controls.DOWN.isPressed()) {
                forceDownTime.setValue(0.3f);
            }
        }
        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        if(Controls.TOGGLE_WORLD.isJustPressed()) {
            ((GameScene)getScene()).toggleWorld();
        }

        if(Controls.POWER.isJustPressed()) {
            beginPower();
        }

        if(Controls.HORIZONTAl.get() != 0) {
//            drawn.setFlippedX(Controls.HORIZONTAl.get() < 0);
        }
    }

    private void beginPower() {
        powerTime.setValue(0.5f);
    }

    private void updateVX(float delta) {
        float vx = accel;
        if(!onGround) {
            vx *= accelAirMultiplier;
        }

        vel.x = UMath.approach(vel.x, Controls.HORIZONTAl.get() * maxRun, vx * delta);
    }


    private void updateVY(float delta) {
        if(onGround) {
            jumpGraceTime.setValue(coyoteTime);
        }

        if(Controls.JUMP.isJustPressed()) {
            jumpMemoryTime.setValue(maxJumpMemoryTime);
        }

        if(jumpGraceTime.isActive() && jumpMemoryTime.isRunning()) { // jump beginning
            jumpGraceTime.clear();
            jumpMemoryTime.clear();
            varJumpTime.setValue(jumpTime);
            vel.y = jumpSpeed;
            vel.x += jumpHorizontalBoost * Controls.HORIZONTAl.get();
        } else {
            float mult;
            if(Controls.JUMP.isPressed() && Math.abs(vel.y) <= halfGravityThreshold) mult = 0.5f;
            else mult = 1;

            vel.y = UMath.approach(vel.y, maxFall, gravity * mult * delta);
        }

        if(varJumpTime.isRunning()) {
            if(Controls.JUMP.isPressed()) {
                vel.y = jumpSpeed;
            } else {
                varJumpTime.clear();
            }
        }
    }

    private void collideX(Collision collision) {
        vel.x = 0;
        zeroRemainderX();
    }

    private void collideY(Collision collision) {
        if(collision.stopper instanceof JumpThru && !forceDownTime.isRunning() && vel.y < 0) {
            collision.stopper.setCollidable(false);
            moveY(-1, this::collideY);
            collision.stopper.setCollidable(true);
        } else {
            vel.y = 0;
            zeroRemainderY();
        }
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
    }
}
