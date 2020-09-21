package com.redsponge.sponge.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.SAnimation;
import com.redsponge.sponge.animation.SAnimationGroup;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.components.DrawnComponent.PositionPolicy;
import com.redsponge.sponge.components.DrawnComponent.SizePolicy;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.game.GameScene.WorldMode;
import com.redsponge.sponge.util.UMath;

public class FirePlayer extends PActor {

    private Vector2 vel;

    private final TimedAction jumpGraceTime;
    private final TimedAction varJumpTime;
    private final TimedAction jumpMemoryTime;
    private final TimedAction forceDownTime;
    private final TimedAction powerTime;
    private final TimedAction powerAfterEffectTime;

    private final TimedAction blockChangeFacingTime;
    private final TimedAction powerCooldownTime;

    // region constants
    private static final float coyoteTime = 0.1f;
    private static final float jumpTime = 0.2f;
    private static final float jumpSpeed = 170;
    private static final float jumpHorizontalBoost = 30;
    private static final float maxFall = 500;
    private static final float gravity = -800;
    private static final float halfGravityThreshold = 40;
    private static final float maxRun = 200;
    private static final float accel = 800;
    private static final float accelAirMultiplier = 0.8f;
    private static final float maxJumpMemoryTime = 0.1f;

    private static final float fireUpBoostPower = 300;
    private static final float powerAccelMultiplier = 0.1f;
    private static final float powerCooldown = 0.4f;

    private static final Color iceWorldCooldownColor = new Color(0.5f, 0.5f, 1, 1);
    private static final Color fireWorldCooldownColor = new Color(1, 0.5f, 0, 1);
    // endregion

    private boolean onGround;
    private DrawnComponent drawn;
    private AnimationComponent attackAnimation;
    private SAnimationGroup attackAnimations;
    private int facing;


    private enum Attack {
        FIRE_DOWN("fire_attack_down") {
            @Override
            void apply(FirePlayer player) {
                player.vel.y = fireUpBoostPower;
                player.vel.x /= 10;
            }
        },
        FIRE_SIDE("fire_attack_side") {
            @Override
            void apply(FirePlayer player) {
                player.vel.x = -200 * player.facing;
                player.vel.y = 50;
                player.blockChangeFacingTime.setValue(0.4f);
            }
        },
        FIRE_UP("fire_attack_up") {
            @Override
            void apply(FirePlayer player) {
                player.vel.y = -fireUpBoostPower;
            }
        }
        ;
        abstract void apply(FirePlayer player);
        private final String animation;

        Attack(String animation) {
            this.animation = animation;
        }

        public String getAnimation() {
            return animation;
        }
    }

    private Attack currentAttack;

    public FirePlayer(Vector2 pos) {
        super(pos);
        getHitbox().set(-4, -4, 16, 16);
        add(jumpGraceTime = new TimedAction());
        add(varJumpTime = new TimedAction());
        add(jumpMemoryTime = new TimedAction());
        add(forceDownTime = new TimedAction());
        add(powerTime = new TimedAction());
        add(powerAfterEffectTime = new TimedAction());
        add(blockChangeFacingTime = new TimedAction());
        add(powerCooldownTime = new TimedAction());
        vel = new Vector2();
        facing = 1;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        Gdx.app.setLogLevel(Application.LOG_INFO);
        attackAnimations = getScene().getAssets().getAnimationGroup("player");
        System.out.println(attackAnimations);
        Gdx.app.log("Test", Boolean.toString(attackAnimation == null));
        SAnimation sfau = attackAnimations.get("fire_attack_up");
        Animation<TextureRegion> fau = sfau.getBuiltAnimation();
        attackAnimation = new AnimationComponent(false, false, attackAnimations.get("fire_attack_up").getBuiltAnimation());
        attackAnimation.setOffsetX(-24 - 32).setOffsetY(-24- 32).setPositionPolicy(PositionPolicy.USE_ENTITY).setSizePolicy(SizePolicy.USE_REGION);

        add(drawn = new DrawnComponent(true, true, getScene().getAssets().<TextureAtlas>get("player.atlas").findRegion("player")));
        add(attackAnimation);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        onGround = groundCheck();

        updateVX(delta);
        updateVY(delta);

        if(!Controls.DOWN.isPressed()) {
            forceDownTime.setValue(0.3f);
        }

        if(attackAnimation.isCompleted()) {
            attackAnimation.setVisible(false);
        }

        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        if(Controls.TOGGLE_WORLD.isJustPressed()) {
            ((GameScene)getScene()).toggleWorld();
        }
        if(Controls.DEBUG.isJustPressed()) {
        }

        if(Controls.POWER.isJustPressed() && !powerCooldownTime.isActive()) {
            beginPower();
        }

        if(!blockChangeFacingTime.isActive() && Controls.HORIZONTAL.get() != 0) {
            drawn.setFlippedX(Controls.HORIZONTAL.get() < 0);
            attackAnimation.setFlippedX(Controls.HORIZONTAL.get() < 0);
            facing = Controls.HORIZONTAL.get();
        }
        drawn.getColor().set(
                UMath.lerp(fireWorldCooldownColor.r, Color.WHITE.r, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                UMath.lerp(fireWorldCooldownColor.g, Color.WHITE.g, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                UMath.lerp(fireWorldCooldownColor.b, Color.WHITE.b, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                1);
    }

    private void beginPower() {
        powerTime.setValue(0.2f);
        powerAfterEffectTime.setValue(0.3f);
        powerCooldownTime.setValue(powerCooldown);

        if(((GameScene)getScene()).getMode() == WorldMode.FIRE) {
            int vertical = Controls.VERTICAL.get();
            if (vertical > 0) {
                currentAttack = Attack.FIRE_DOWN;
            } else if (vertical < 0) {
                currentAttack = Attack.FIRE_UP;
            } else {
                currentAttack = Attack.FIRE_SIDE;
            }
        }
        currentAttack.apply(this);

        attackAnimation.setAnimation(attackAnimations.get(currentAttack.getAnimation()).getBuiltAnimation(), true);
        attackAnimation.update(0);
        attackAnimation.setActive(true);
        attackAnimation.setVisible(true);
    }

    private void updateVX(float delta) {
        float vx = accel;
        if(!onGround) {
            vx *= accelAirMultiplier;
        }
        if(powerTime.isActive()) {
            vx *= powerAccelMultiplier;
        }

        vel.x = UMath.approach(vel.x, Controls.HORIZONTAL.get() * maxRun, vx * delta);
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
            vel.x += jumpHorizontalBoost * Controls.HORIZONTAL.get();
        } else {
            final float multiplier;
            final float powerMultiplier;

            if(Controls.JUMP.isPressed() && Math.abs(vel.y) <= halfGravityThreshold) multiplier = 0.5f;
            else multiplier = 1;

            if(powerAfterEffectTime.isActive()) powerMultiplier = 0.1f;
            else powerMultiplier = 1;

            vel.y = UMath.approach(vel.y, maxFall, gravity * powerMultiplier * multiplier * delta);
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
