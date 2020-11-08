package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.animation.SAnimationGroup;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.screen.Scene;

public class Torch extends Entity {

    private AnimationComponent drawnFire;
    private AnimationComponent drawnHolder;

    public Torch(Vector2 pos) {
        super(pos);
        setWidth(8*2);
        setHeight(13*2);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        SAnimationGroup torchAnimations = scene.getAssets().getAnimationGroup("torch");
        drawnHolder = new AnimationComponent(true, true, torchAnimations.get("holder").getBuiltAnimation());
        drawnFire = new AnimationComponent(true, true, torchAnimations.get("fire").getBuiltAnimation());

        drawnHolder.setScaleX(2).setScaleY(2);
        drawnFire.setScaleX(2).setScaleY(2);

        add(drawnHolder);
        add(drawnFire);

        scene.getRenderingPipeline().getEffect(BloomEffect.class).addBloomRender(() -> {
            drawnFire.getColor().a = 0.5f;
            drawnFire.render();
            drawnFire.getColor().a = 1;
        });
    }
}
