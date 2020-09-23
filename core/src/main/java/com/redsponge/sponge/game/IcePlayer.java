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
import com.redsponge.sponge.components.AnimationStreamComponent;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.components.DrawnComponent.PositionPolicy;
import com.redsponge.sponge.components.DrawnComponent.SizePolicy;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.JumpThru;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.physics.PTrigger;
import com.redsponge.sponge.physics.Trigger;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class IcePlayer extends PActor {
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

    private static final float powerAccelMultiplier = 0.1f;
    private static final float powerCooldown = 0.4f;

    private static final Color iceWorldCooldownColor = new Color(0.5f, 0.5f, 1, 1);
    // endregion

    private boolean onGround;
    private DrawnComponent drawn;
    private AnimationStreamComponent attackAnimation;
    private SAnimationGroup attackAnimations;
    private int facing;

    private boolean isSlowFalling;
    private boolean isSpikeFalling;
    private boolean isSpikePrepping;
    private int inSteamCount;

    public IcePlayer(Vector2 pos) {
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
        Gdx.app.log("Test", Boolean.toString(attackAnimation == null));
        attackAnimation = new AnimationStreamComponent(false, false, attackAnimations.get("slow_fall").getBuiltAnimation());
        attackAnimation.setOffsetX(-24 - 32).setOffsetY(-24 - 32).setPositionPolicy(PositionPolicy.USE_ENTITY).setSizePolicy(SizePolicy.USE_REGION);
        setOnTrigger(this::onTrigger);

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

        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        if(Controls.TOGGLE_WORLD.isJustPressed()) {
            ((GameScene)getScene()).toggleWorld();
        }

        if(Controls.DEBUG.isJustPressed()) {
        }

        if(Controls.POWER.isJustPressed() && !isPowerActive() && !onGround) {
            beginPower();
        }
        if(!Controls.POWER.isPressed()) {
            endPower();
        }

        if(!blockChangeFacingTime.isActive() && Controls.HORIZONTAL.get() != 0) {
            drawn.setFlippedX(Controls.HORIZONTAL.get() < 0);
            attackAnimation.setFlippedX(Controls.HORIZONTAL.get() < 0);
            facing = Controls.HORIZONTAL.get();
        }
        drawn.getColor().set(
                UMath.lerp(iceWorldCooldownColor.r, Color.WHITE.r, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                UMath.lerp(iceWorldCooldownColor.g, Color.WHITE.g, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                UMath.lerp(iceWorldCooldownColor.b, Color.WHITE.b, (powerCooldown - powerCooldownTime.getValue()) / powerCooldown),
                1);
    }

    private void endPower() {
        if(isSlowFalling) {
            attackAnimation.setVisible(false);
        } else if(isSpikeFalling) {
            attackAnimation.setVisible(false);
            attackAnimation.setActive(false);
            powerAfterEffectTime.setValue(0.3f);
            getScene().getAssets().getParticle("ice_particle").spawnEffect(getX() + 4, getY() - 30);
            //TODO: spike bam particles
        } else if(isSpikePrepping) {
            System.out.println("Prep out");
            attackAnimation.setAnimationSpeed(-1);
            attackAnimation.setNextAnimation(null);
            attackAnimation.setNoNextAnimation(true);
            attackAnimation.setOnAnimationSwitch(() -> attackAnimation.setAnimationSpeed(1));
            powerAfterEffectTime.setValue(0.3f);
        }
        isSlowFalling = false;
        isSpikePrepping = false;
        isSpikeFalling = false;
    }

    private void beginPower() {
        attackAnimation.setAnimationSpeed(1);
        attackAnimation.setOnAnimationSwitch(null);
        if(Controls.VERTICAL.get() == -1) {
            beginSpike();
        } else {
            beginSlowfall();
        }

        attackAnimation.update(0);
        attackAnimation.setActive(true);
        attackAnimation.setVisible(true);
    }

    private void beginSlowfall() {
        System.out.println("SLOWFALL");
        isSlowFalling = true;
        vel.y = -0.1f;
        attackAnimation.setAnimation(attackAnimations.get("slow_fall").getBuiltAnimation());
    }

    private void beginSpike() {
        attackAnimation.setAnimation(attackAnimations.get("spike_begin").getBuiltAnimation(), true);
        attackAnimation.resetTime();
        attackAnimation.setNextAnimation(attackAnimations.get("spike_loop").getBuiltAnimation());
        attackAnimation.setOnAnimationSwitch(() -> {
            isSpikeFalling = true;
            isSpikePrepping = false;
            attackAnimation.setOnAnimationSwitch(null);
        });
        vel.y = 0;
        vel.x = 0;
        isSpikePrepping = true;
        isSpikeFalling = false;
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
        if(isSpikeFalling || isSpikePrepping) vel.x = 0;
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

            float slowFallMultiplier = 1;
//            if(isSlowFalling && vel.y < 0 && inSteamCount == 0) slowFallMultiplier = 0.01f;
//            else slowFallMultiplier = 1;

            float spikeFallMultiplier;
            if(isSpikePrepping) spikeFallMultiplier = 0;
            else if(isSpikeFalling) spikeFallMultiplier = 3;
            else spikeFallMultiplier = 1;
            if(isSlowFalling && inSteamCount == 0 && vel.y < 0) {
                vel.y /= 1.5f;
                if(vel.y > -10) vel.y = -10;
            }

            if(inSteamCount > 0) vel.y += 30;
            if(vel.y > 150) vel.y = 150;

            vel.y = UMath.approach(vel.y, maxFall, gravity * spikeFallMultiplier * powerMultiplier * multiplier * delta * slowFallMultiplier);
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
        if(collision.stopper instanceof PTrigger) {
            System.out.println("TRIGGER");
        }
    }

    private void collideY(Collision collision) {
        endPower();
        if(collision.stopper instanceof JumpThru && !forceDownTime.isRunning() && vel.y < 0) {
            collision.stopper.setCollidable(false);
            moveY(-1, this::collideY);
            collision.stopper.setCollidable(true);
        } else {
            vel.y = 0;
            zeroRemainderY();
        }
    }

    private void onTrigger(Trigger t) {
        if(t.trigger instanceof SteamColumn) {
            if(t.isEnter) {
                inSteamCount++;
            } else {
                inSteamCount--;
            }
        }
        if(t.trigger instanceof Killer) {
            System.out.println("DEATH");
        }
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
    }

    public boolean isPowerActive() {
        return isSlowFalling || isSpikePrepping || isSpikeFalling;
    }

    public Vector2 getVelocity() {
        return vel;
    }
}
