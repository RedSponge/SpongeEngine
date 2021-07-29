package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.test.Attack;
import com.redsponge.sponge.test.PresentationSettings;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.Logger;

public class Enemy extends PActor {

    private AnimationComponent drawn;
    private AnimationNodeSystem system;

    private Vector2 vel;
    private float gravity = -500;

    private DelayedRemovalArray<Attack> attackingRectangles;

    private ShaderProgram shaderProgram;
    private final TimedAction flashTime;
    private final float maxFlashTime = 0.4f;
    private Sound hitSound;
    private boolean facingRight;

    public Enemy(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 27, 34);
        vel = new Vector2();
        attackingRectangles = new DelayedRemovalArray<>();

        add(flashTime = new TimedAction());
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        system = scene.getAssets().getAnimationNodeSystemInstance("enemy");
        add(drawn = new AnimationComponent(true, true, system.getActiveAnimation()));
        drawn.setOffsetX(-20);
        drawn.setOffsetY(-8);

        shaderProgram = new ShaderProgram(Gdx.files.internal("flash.vert"), Gdx.files.internal("flash.frag"));
        Logger.info(this, shaderProgram.isCompiled());
        Logger.info(this, shaderProgram.getLog());

        hitSound = scene.getAssets().get("hit.ogg");
        EventBus.getInstance().registerListener(this);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        vel.y += gravity * delta;
        vel.x *= 0.9f;
        if(Math.abs(vel.x) < 0.1f) vel.x = 0;
        if(vel.x != 0) {
            facingRight = vel.x < 0;
        }
        drawn.setFlippedX(facingRight);

        for (int i = 0; i < attackingRectangles.size; i++) {
            Rectangle rect = attackingRectangles.get(i).attackRect;
            if(getSceneHitbox().intersects(new Hitbox((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height))) {
                getHit(attackingRectangles.get(i).dir);
                attackingRectangles.removeIndex(i);
            }
        }

        boolean isOnGround = groundCheck();

        moveX(vel.x * delta, null);
        moveY(vel.y * delta, this::onCollideY);

        system.putParam("is_on_ground", isOnGround);
        system.update();
        drawn.setAnimation(system.getActiveAnimation());
    }

    private void getHit(Vector2 dir) {
        vel.x = dir.x;
        vel.y = dir.y;
        flashTime.setValue(maxFlashTime);
        EventBus.getInstance().dispatch(new RequireFreezeEvent((Math.abs(vel.y) > 100) ? 0.12f : 0.07f));
        EventBus.getInstance().dispatch(new ShakeEvent(0.05f, false));
        if(PresentationSettings.doSound) hitSound.play(0.5f, MathUtils.random(1.7f, 2), 0);
        getScene().getAssets().getParticle("stars").spawnEffect(getX() + getWidth() / 2f + (facingRight ? 0 : 8), getY());
    }

    private void onCollideY(Collision collision) {
        vel.y = 0;
    }

    @Override
    public void render() {
        ShaderProgram lastShader = SpongeGame.i().getBatch().getShader();
        SpongeGame.i().getBatch().setShader(shaderProgram);
        shaderProgram.setUniformf("flashAmount", Interpolation.exp5Out.apply(0, maxFlashTime, flashTime.getValue()) * 2);
        super.render();
        SpongeGame.i().getBatch().setShader(lastShader);
//        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
    }

    @EventHandler
    public void onBeginAttack(AttackEvent event) {
        if(event.caller != this) attackingRectangles.add(event.attack);
    }

    @EventHandler
    public void onEndAttack(AttackEndEvent event) {
        for (int i = 0; i < attackingRectangles.size; i++) {
            if(attackingRectangles.get(i).attackRect == event.rectangle) {
                attackingRectangles.removeIndex(i);
            }
        }
    }
}
