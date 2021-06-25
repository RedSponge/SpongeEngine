package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.DrawnComponent.PositionPolicy;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.event.CollisionEvent;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.Logger;
import com.redsponge.sponge.util.UMath;

public class Player extends PActor {

    enum AttackDirection {
        Front,
        Up,
        Down
    };

    private AttackDirection attackDir;
    private Vector2 vel;
    private Vector2 attackVel;

    private TimedAction jumpGraceTime;
    private TimedAction varJumpTime;
    private TimedAction jumpMemoryTime;
    private TimedAction forceDownTime;
    private TimedAction attackTime;

    private PlayerControls controls;

    private final float coyoteTime = 0.1f;
    private final float jumpTime = 0.2f;
    private final float jumpSpeed = 180;
    private final float jumpHorizontalBoost = 30;
    private final float maxFall = -200;
    private final float gravity = 600;
    private final float halfGravityThreshold = 30;
    private final float maxRun = 150;
    private final float accel = 800;
    private final float accelAirMultiplier = 0.8f;
    private final float maxJumpMemoryTime = 0.1f;

    private boolean onGround;

    private AnimationComponent drawn;
    private AnimationNodeSystem system;
    private PunchEvent punchEventToSend;

    private boolean isFacingLeft;

    private PunchableComponent punchable;

    public Player(Vector2 pos, PlayerControls controls) {
        super(pos);
        this.controls = controls;
        getHitbox().set(-4, -8, 16, 30);
        add(jumpGraceTime = new TimedAction());
        add(varJumpTime = new TimedAction());
        add(jumpMemoryTime = new TimedAction());
        add(forceDownTime = new TimedAction());
        add(attackTime = new TimedAction());
        vel = new Vector2();
        attackVel = new Vector2();
        setzIndex(10);

        add(punchable = new PunchableComponent(true, true, vel, 300));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);

        EventBus.getInstance().registerListener(this);
        //animations = getScene().getAssets().getAnimationGroup("player");
        system = getScene().getAssets().getAnimationNodeSystemInstance("player");
//        system.addNodes(Gdx.files.internal("test/animation/player.animnodes"));
        add(drawn = new AnimationComponent(true, true, system.getActiveBuiltAnimation()));
        drawn.setPositionPolicy(PositionPolicy.USE_ENTITY);
        drawn.setOffsetX(-8);
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    public void attack() {
        int vertical = controls.getVertical().get();
        String animationName;

        switch (vertical) {
            case -1: {
                attackDir = AttackDirection.Down;
                punchEventToSend = new PunchEvent(new Hitbox(getX() - 10, getY() - 60, getWidth() + 20, 70), this, new Vector2(getWidth() / 2f + 10, 70));
                animationName = "attack_down";
            } break;
            case  0: {
                attackDir = AttackDirection.Front;
                if(isFacingLeft) {
                    punchEventToSend = new PunchEvent(new Hitbox(getX() - 60, getY() - 10, 50 + getWidth(), getHeight() + 20), this, new Vector2(getWidth() + 50 + 50, 0));
                } else {
                    punchEventToSend = new PunchEvent(new Hitbox(getX() + 10, getY() - 10, 50 + getWidth(), getHeight() + 20), this, new Vector2(-50, 0));
                }
                animationName = "attack_front";
            } break;
            case 1: {
                attackDir = AttackDirection.Up;
                punchEventToSend = new PunchEvent(new Hitbox(getX() - 5, getY() - 5, getWidth() + 10, 90 + getHeight() - 20), this, new Vector2(getWidth() / 2f + 5, 0));
                animationName = "attack_up";
            } break;
            default:
                throw new RuntimeException("Invalid vertical value on attack(): " + vertical);
        }

//        attackVel.set(vel).setLength(50);
        attackTime.setValue(system.getAnimationGroup().get(animationName).getBuiltAnimation().getAnimationDuration());
        EventBus.getInstance().dispatch(punchEventToSend);
        vel.scl(0.5f);
        if(vel.y < 0) vel.y = -.1f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        onGround = groundCheck();


        if(controls.getAttack().isJustPressed() && !attackTime.isRunning()) {
            attack();
        }

        if(controls.getHorizontal().get() != 0) {
            isFacingLeft = controls.getHorizontal().get() < 0;
            drawn.setFlippedX(isFacingLeft);
        }

        Logger.info(this, "Begin");
        Logger.info(this, "VX is "  +vel.x);
        if(attackTime.isRunning()) {
//            vel.set(attackVel);
        } else {
            updateVY(delta);
            updateVX(delta);

            if(!controls.getDown().isPressed()) {
                forceDownTime.setValue(0.3f);
            }
        }

        Logger.info(this, "After update VX is " + vel.x);

        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        Logger.info(this, "After move VX is " + vel.x);

        Logger.info(this, "END");

        system.putParam("is_attacking_up", attackTime.isRunning() && attackDir == AttackDirection.Up);
        system.putParam("is_attacking_front", attackTime.isRunning() && attackDir == AttackDirection.Front);
        system.putParam("is_attacking_down", attackTime.isRunning() && attackDir == AttackDirection.Down);
        system.putParam("x_speed", vel.x);
        system.putParam("y_speed", vel.y);
        system.putParam("is_on_ground", onGround);

        system.update();

        drawn.setAnimation(system.getActiveBuiltAnimation());
        Vector2 offset = system.getActiveAnimation().getOffsets()[isFacingLeft ? 1 : 0];
        drawn.setOffsetX(offset.x).setOffsetY(offset.y);
        drawn.update(0);
    }

    private void updateVX(float delta) {
        float vx = accel;
        if(!onGround) {
            vx *= accelAirMultiplier;
        }

        vel.x = UMath.approach(vel.x, controls.getHorizontal().get() * maxRun, vx * delta);
    }


    private void updateVY(float delta) {
        if(onGround) {
            jumpGraceTime.setValue(coyoteTime);
        }

        if(controls.getJump().isJustPressed()) {
            jumpMemoryTime.setValue(maxJumpMemoryTime);
        }

        if(jumpGraceTime.isActive() && jumpMemoryTime.isRunning()) { // jump beginning
            jumpGraceTime.clear();
            jumpMemoryTime.clear();
            varJumpTime.setValue(jumpTime);
            vel.y = jumpSpeed;
            vel.x += jumpHorizontalBoost * controls.getHorizontal().get();
        } else {
            float mult;
            if(controls.getJump().isPressed()) {
                if(vel.y > 0) {
                    if(Math.abs(vel.y) <= halfGravityThreshold) mult = 0.5f;
                    else mult = 1;
                } else mult = 1.5f;
            }
            else mult = 2f;

            vel.y = UMath.approach(vel.y, maxFall, gravity * mult * delta);
        }

        if(varJumpTime.isRunning()) {
            if(controls.getJump().isPressed()) {
                vel.y = jumpSpeed;
            } else {
                varJumpTime.clear();
            }
        }
    }

    private void collideX(Collision collision) {
        vel.x = 0;
        zeroRemainderX();

        Logger.info(this, "Hit X!");
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
        if(punchEventToSend != null) {
            SpongeGame.i().getShapeDrawer().rectangle(punchEventToSend.getPunchBox().getRectangle(), Color.YELLOW);
            SpongeGame.i().getShapeDrawer().filledCircle(punchEventToSend.getOrigin(), 2, Color.RED);
        }
    }

}
