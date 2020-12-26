package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.animation.SAnimationGroup;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

public class IsometricPlayerRenderer extends Entity {

    private Vector2 vel;

    private Texture upTex, downTex;

    private AnimationComponent drawn;
    private Vector2 referencePos, referenceVel;
    private Vector2 requiredByReference;
    private IsometricTileMapRenderer mapRenderer;

    private SAnimationGroup playerAnimations;
    private Animation<TextureRegion> frontAnimation, backAnimation;
    private TimedAction jumpingTime;
    private float jumpLength = 0.6f;
    private float jumpHeight = 10;

    public IsometricPlayerRenderer(Vector2 pos, Vector2 refPos, Vector2 refVel) {
        super(pos);
        vel = new Vector2();
        this.referencePos = refPos;
        this.referenceVel = refVel;
        this.requiredByReference = new Vector2();
        add(jumpingTime = new TimedAction());
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
//        upTex = scene.getAssets().get("player_up.png");
//        downTex = scene.getAssets().get("player_down.png");

        playerAnimations = scene.getAssets().getAnimationGroup("player");

        frontAnimation = playerAnimations.get("front").getBuiltAnimation();
        backAnimation = playerAnimations.get("back").getBuiltAnimation();
        drawn = new AnimationComponent(true, true, backAnimation);
        add(drawn);
        mapRenderer = scene.first(IsometricTileMapRenderer.class);

        EventBus.getInstance().registerListener(this);
    }

    @Override
    public void update(float delta) {
        mapRenderer.getPositionOfIndex((int) referencePos.x, (int) referencePos.y, requiredByReference);
        getPositionf().x = UMath.approach(getPositionf().x, requiredByReference.x + 14, 50 * delta);
        getPositionf().y = UMath.approach(getPositionf().y, requiredByReference.y + 7, 50 * delta / 2);
        if(getPositionf().x ==  requiredByReference.x + 14 && getPositionf().y == requiredByReference.y + 7) {
            System.out.println("STAHP");
            mapRenderer.getLevel().progressPlayer();
//            drawn.resetTime();
        }
//        setX(requiredByReference.x + 14);
//        setY(requiredByReference.y + 7);
        float a = jumpLength;
        float h = jumpHeight;
        float x = jumpingTime.getValue();
        float jumpOffset = -4 * h * x * (x - a) / (a * a);
        drawn.setOffsetX(-4-1);
        drawn.setOffsetY(-2-1 + jumpOffset);

        if(referenceVel.x != 0 || referenceVel.y != 0) {
            boolean up = referenceVel.x + referenceVel.y > 0;
            boolean flipped = referenceVel.y > 0 || referenceVel.x < 0;
            drawn.setAnimation(up ? backAnimation : frontAnimation);
            drawn.setFlippedX(flipped);
        }
        drawn.getColor().set(mapRenderer.getLevel().isPlayerProtected() ? Color.BLUE : Color.WHITE);

        super.update(delta);
    }

    @EventHandler
    public void onJump(JumpEvent event) {
        jumpingTime.setValue(jumpLength);
    }
}
