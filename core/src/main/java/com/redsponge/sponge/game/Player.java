package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.game.hair.HairComponent;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.UMath;

import java.util.ArrayList;

public class Player extends PActor {

    private AnimationComponent drawn;
    private AnimationNodeSystem animNodeSystem;
    private PlayerComponent playerComponent;
    private HairComponent hairComponent;

    private float scale;
    private float time;
    private TimedAction bigTimer;
    private TimedAction smallTimer;
    private final float bigTimeValue = 5;
    private final float smallTimeValue = 5;
    private float currentSize = 1;

    private ArrayList<GrowCake> tmpCakes;
    private ArrayList<ShrinkBottle> tmpBottles;


    public Player() {
        super(new Vector2(360 / 2f, 300));
//        getHitbox().set(0, 0, 16 * scale, 28 * scale);

        tmpCakes = new ArrayList<>();
        tmpBottles = new ArrayList<>();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        animNodeSystem = scene.getAssets().getAnimationNodeSystemInstance("player");

        drawn = new AnimationComponent(true, true, animNodeSystem.getAnimationGroup().get("glide").getBuiltAnimation());

        add(playerComponent = new PlayerComponent(true, false));
        add(hairComponent = new HairComponent(true, true, new Vector2(8, 26), 6));
        add(drawn);
        add(bigTimer = new TimedAction());
        add(smallTimer = new TimedAction());

//        setScale(2);
    }

    public void setScale(float scale) {
        this.scale = scale;
        getHitbox().set(0, 0, (int) (16 * scale), (int) (32 * scale));
        drawn.setScaleX(scale).setScaleY(scale);
        hairComponent.setScale(scale);
    }

    @Override
    public void update(float delta) {
        animNodeSystem.putParam("is_gliding", playerComponent.isGliding());
        animNodeSystem.update();

        super.update(delta);

        drawn.setAnimation(animNodeSystem.getActiveAnimation());

        float glidingOffset = -7 * scale;
        TextureRegion reg = drawn.getRendered();
        drawn.setOffsetX(-reg.getRegionWidth() / 2f * scale + getWidth() / 2f);
        drawn.setOffsetY(-reg.getRegionHeight() / 2f * scale + getHeight() / 2f + (playerComponent.isGliding() ? glidingOffset : 0));

        if(playerComponent.getVX() != 0) {
            drawn.setFlippedX(playerComponent.getVX() < 0);
            hairComponent.setFlipped(playerComponent.getVX() < 0);
        }

        time += delta;
        Hitbox hitbox = getSceneHitbox();
        tmpCakes.clear();
        getScene().all(tmpCakes, GrowCake.class).forEach(cake -> {
            if(cake.getSceneHitbox().intersects(hitbox)) {
                cake.removeSelf();
                this.bigTimer.setValue(bigTimeValue);
            }
        });

        tmpBottles.clear();
        getScene().all(tmpBottles, ShrinkBottle.class).forEach(bottle -> {
            if(bottle.getSceneHitbox().intersects(hitbox)) {
                bottle.removeSelf();
                this.smallTimer.setValue(smallTimeValue);
            }
        });

        float requiredSize = 1;
        if(bigTimer.isActive()) {
            requiredSize *= 2;
        }
        if(smallTimer.isActive()) {
            requiredSize /= 2f;
        }

        currentSize = UMath.lerp(currentSize, requiredSize, 0.1f);
        if(Math.abs(currentSize - requiredSize) < 0.1f) {
            currentSize = requiredSize;
        }
        setScale(currentSize);
    }

    @Override
    public void render() {
        super.render();
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }

    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }
}
