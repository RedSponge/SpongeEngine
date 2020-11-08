package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.DrawnComponent.PositionPolicy;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class Player extends PActor {

    private Vector2 vel;

    private TimedAction jumpGraceTime;
    private TimedAction varJumpTime;
    private TimedAction jumpMemoryTime;
    private TimedAction forceDownTime;
    private TimedAction attackTime;

    private final float coyoteTime = 0.1f;
    private final float jumpTime = 0.2f;
    private final float jumpSpeed = 160;
    private final float jumpHorizontalBoost = 30;
    private final float maxFall = 160;
    private final float gravity = -1000;
    private final float halfGravityThreshold = 40;
    private final float maxRun = 200;
    private final float accel = 800;
    private final float accelAirMultiplier = 0.8f;
    private final float maxJumpMemoryTime = 0.1f;

    private boolean onGround;

    private AnimationComponent drawn;
    private AnimationNodeSystem system;

    public Player(Vector2 pos) {
        super(pos);
        getHitbox().set(-4, -8, 16, 30);
        add(jumpGraceTime = new TimedAction());
        add(varJumpTime = new TimedAction());
        add(jumpMemoryTime = new TimedAction());
        add(forceDownTime = new TimedAction());
        add(attackTime = new TimedAction());
        vel = new Vector2();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        //animations = getScene().getAssets().getAnimationGroup("player");
        system = getScene().getAssets().getAnimationNodeSystemInstance("player");
//        system.addNodes(Gdx.files.internal("test/animation/player.animnodes"));
        add(drawn = new AnimationComponent(true, true, system.getActiveAnimation()));
        drawn.setPositionPolicy(PositionPolicy.USE_ENTITY);
        drawn.setOffsetX(-8);
        BloomEffect be = scene.getRenderingPipeline().getEffect(BloomEffect.class);
        be.addBloomRender(() -> {
            drawn.getColor().set(Color.BLACK);
            render();
            drawn.getColor().set(Color.WHITE);
        });
    }

    public void attack() {
        attackTime.setValue(system.getAnimationGroup().get("attack_up").getBuiltAnimation().getAnimationDuration());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        onGround = groundCheck();


        if(Controls.ATTACK.isJustPressed() && !attackTime.isRunning()) {
            attack();
        }

        if(attackTime.isRunning()) {
            vel.y = 0;
        } else {
            updateVY(delta);
            updateVX(delta);

            if(!Controls.DOWN.isPressed()) {
                forceDownTime.setValue(0.3f);
            }

            moveX(vel.x * delta, this::collideX);
            moveY(vel.y * delta, this::collideY);
        }

        if(Controls.HORIZONTAl.get() != 0) {
            drawn.setFlippedX(Controls.HORIZONTAl.get() < 0);
        }

        system.putParam("is_attacking", attackTime.isRunning());
        system.putParam("x_speed", vel.x);
        system.putParam("y_speed", vel.y);
        system.putParam("is_on_ground", onGround);

        system.update();

        drawn.setAnimation(system.getActiveAnimation());
        if(attackTime.isRunning()) {
            drawn.setOffsetX(-19);
        } else {
            drawn.setOffsetX(-8);
        }
        drawn.update(0);
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
//        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
    }
}
