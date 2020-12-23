package com.redsponge.sponge.test;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.redsponge.sponge.test.presentation.AttackEndEvent;
import com.redsponge.sponge.test.presentation.AttackEvent;
import com.redsponge.sponge.util.Logger;
import com.redsponge.sponge.util.UMath;

public class Player extends PActor {

    private Vector2 vel;

    private final TimedAction jumpGraceTime;
    private final TimedAction varJumpTime;
    private final TimedAction jumpMemoryTime;
    private final TimedAction forceDownTime;
    private final TimedAction attackTime;
    private final TimedAction attackCooldown;
    private final TimedAction lungeTime;
    private final TimedAction comboTime;
    private final TimedAction attackEventSendTime;

    private final float coyoteTime = 0.1f;
    private final float jumpTime = 0.2f;
    private final float jumpSpeed = 220;
    private final float jumpHorizontalBoost = 30;
    private final float maxFall = 220;
    private final float gravity = -1000;
    private final float halfGravityThreshold = 40;
    private final float maxRun = 200;
    private final float accel = 800;
    private final float accelAirMultiplier = 0.8f;
    private final float maxJumpMemoryTime = 0.1f;
    private final float inAttackSpeedMultiplier = 0.5f;
    private final float attackCooldownConstant = 0.05f;
    private final float lungeBoostTime = 0.1f;
    private final float lungePower = 500;
    private final float comboMaxTime = 0.2f;

    private static final int LUNGE_ATTACK = 0;

    private boolean onGround;

    private Rectangle attackRectangle;

    private AnimationComponent drawn;
    private AnimationNodeSystem system;

    private int attackCount;
    private TimedAction attackPressMemory;

    private Sound[] swingSounds;

    public Player(Vector2 pos) {
        super(pos);
        getHitbox().set(-4, -8, 27, 30);
        add(jumpGraceTime = new TimedAction());
        add(varJumpTime = new TimedAction());
        add(jumpMemoryTime = new TimedAction());
        add(forceDownTime = new TimedAction());
        add(attackTime = new TimedAction());
        add(attackCooldown = new TimedAction());
        add(attackPressMemory = new TimedAction());
        add(lungeTime = new TimedAction());
        add(comboTime = new TimedAction());
        add(attackEventSendTime = new TimedAction());

        attackEventSendTime.setOnComplete(this::updateAttackBox);

        vel = new Vector2();
        attackRectangle = new Rectangle();
    }

    private void updateAttackBox() {
        attackRectangle.set(getSceneHitbox().getRectangle());
        attackRectangle.x += 15 * (drawn.isFlippedX() ? 1 : -1);
        if (attackCount == 0) {
            attackRectangle.y += 10;
            attackRectangle.height = 20;
            attackRectangle.width = 40;
        } else if (attackCount == 1 || attackCount == 2) {
            attackRectangle.height = 60;
            attackRectangle.width = 50;
        }
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);

        EventBus.getInstance().registerListener(this);
        //animations = getScene().getAssets().getAnimationGroup("player");
        system = getScene().getAssets().getAnimationNodeSystemInstance("knight");
//        system.addNodes(Gdx.files.internal("test/animation/player.animnodes"));
        add(drawn = new AnimationComponent(true, true, system.getActiveAnimation()));
        drawn.setPositionPolicy(PositionPolicy.USE_ENTITY);
        drawn.setOffsetY(-8);

        swingSounds = new Sound[1];
        for (int i = 0; i < swingSounds.length; i++) {
            System.out.println("swipe" + (i + 1));
            swingSounds[i] = scene.getAssets().<Sound>get("swoop" + (i + 1) + ".ogg");
        }
//        BloomEffect be = scene.getRenderingPipeline().getEffect(BloomEffect.class);
//        be.addBloomRender(() -> {
//            drawn.getColor().set(Color.BLACK);
//            render();
//            drawn.getColor().set(Color.WHITE);
//        });
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    public void attack() {
        if (comboTime.isRunning()) {
            attackCount = (attackCount + 1) % 3;
        } else {
            attackCount = 1;
        }

        attackTime.setValue(system.getAnimationGroup().get("attack" + (attackCount + 1)).getBuiltAnimation().getAnimationDuration());
        if (attackCount == LUNGE_ATTACK) {
            lungeTime.setValue(lungeBoostTime);
        }
        attackCooldown.setValue(attackTime.getValue() + attackCooldownConstant);
        comboTime.setValue(attackCooldown.getValue() + comboMaxTime);
        attackEventSendTime.setValue(0.05f);
        attackEventSendTime.setOnComplete(() -> EventBus.getInstance().dispatch(new AttackEvent(this, new Attack(new Vector2((attackCount == LUNGE_ATTACK ? 300 : 75) * (drawn.isFlippedX() ? 1 : -1), (attackCount == LUNGE_ATTACK ? 200 : 100)), attackRectangle))));
        attackTime.setOnComplete(() -> EventBus.getInstance().dispatch(new AttackEndEvent(attackRectangle)));
        if(PresentationSettings.doSound) swingSounds[MathUtils.random(swingSounds.length - 1)].play(0.2f, MathUtils.random(0.5f, 0.7f) + (attackCount == LUNGE_ATTACK ? +0.4f : 0), 0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        updateAttackBox();

        onGround = groundCheck();


        if (Controls.ATTACK.isJustPressed()) {
            attackPressMemory.setValue(0.05f);
        }
        if (attackPressMemory.isActive() && !attackTime.isRunning() && !attackCooldown.isActive()) {
            attack();
            attackPressMemory.setValue(0);
        }

        updateVX(delta);
        updateVY(delta);

        if (!Controls.DOWN.isPressed()) {
            forceDownTime.setValue(0.3f);
        }

        float finalVelX = vel.x;
        float finalVelY = vel.y;
        if (attackTime.isActive()) {
            finalVelX *= inAttackSpeedMultiplier;
        }
        if (lungeTime.isActive()) {
            finalVelX = lungePower * (drawn.isFlippedX() ? 1 : -1);
            vel.y = Math.max(vel.y, -20);
        }

        moveX(finalVelX * delta, this::collideX);
        moveY(finalVelY * delta, this::collideY);

        if (Controls.HORIZONTAl.get() != 0) {
            drawn.setFlippedX(Controls.HORIZONTAl.get() > 0);
        }

        system.putParam("is_attacking", attackTime.isRunning());
        system.putParam("x_speed", vel.x);
        system.putParam("y_speed", vel.y);
        system.putParam("is_on_ground", onGround);
        system.putParam("attack_index", attackCount + 1);

        system.update();

        drawn.setAnimation(system.getActiveAnimation());
        if (attackTime.isRunning() && attackCount == LUNGE_ATTACK) {
            drawn.setOffsetX(-32 + (drawn.isFlippedX() ? 0 : -4));
        } else if (attackTime.isRunning()) {
            drawn.setOffsetX(-19);
        } else {
            drawn.setOffsetX(-32 + 13.5f);
        }
        drawn.update(0);
    }

    private void updateVX(float delta) {
        float vx = accel;
        if (!onGround) {
            vx *= accelAirMultiplier;
        }
        if (attackTime.isActive()) {
            vx *= inAttackSpeedMultiplier;
        }
        if (lungeTime.isActive()) {
            vx = 0;
        }

        vel.x = UMath.approach(vel.x, Controls.HORIZONTAl.get() * maxRun, vx * delta);
    }


    private void updateVY(float delta) {
        if (onGround) {
            jumpGraceTime.setValue(coyoteTime);
        }

        if (Controls.JUMP.isJustPressed()) {
            jumpMemoryTime.setValue(maxJumpMemoryTime);
        }

        if (jumpGraceTime.isActive() && jumpMemoryTime.isRunning()) { // jump beginning
            jumpGraceTime.clear();
            jumpMemoryTime.clear();
            varJumpTime.setValue(jumpTime);
            vel.y = jumpSpeed;
            vel.x += jumpHorizontalBoost * Controls.HORIZONTAl.get();
        } else {
            float mult;
            if (Controls.JUMP.isPressed() && Math.abs(vel.y) <= halfGravityThreshold) mult = 0.5f;
            else mult = 1;

            vel.y = UMath.approach(vel.y, maxFall, gravity * mult * delta);
        }

        if (varJumpTime.isRunning()) {
            if (Controls.JUMP.isPressed()) {
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
        if (collision.stopper instanceof JumpThru && !forceDownTime.isRunning() && vel.y < 0) {
            collision.stopper.setCollidable(false);
            moveY(-1, this::collideY);
            collision.stopper.setCollidable(true);
        } else {
            if (vel.y < 0 && collision.dir.y < 0) {
//                EventBus.getInstance().dispatch(new ShakeEvent(0.1f, false));
            }
            vel.y = 0;
            zeroRemainderY();
        }
    }

    @Override
    public void render() {
        super.render();
//        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
//        SpongeGame.i().getShapeDrawer().rectangle(attackRectangle, Color.RED);
    }

    @EventHandler
    public void onCollision(CollisionEvent event) {
        Logger.info(this, event);
    }

}
