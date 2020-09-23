package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.physics.PTrigger;
import com.redsponge.sponge.screen.Scene;

public class Fireball extends PTrigger implements Killer {

    private AnimationComponent anim;
    private float time;

    public Fireball(Vector2 pos) {
        super(pos);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        anim = new AnimationComponent(true, true, scene.getAssets().getAnimationGroup("world").get("fireball").getBuiltAnimation());
        anim.setScaleX(2).setScaleY(2);
        anim.setOffsetX(-5);
        anim.setOffsetY(-5);
        getHitbox().set(0, 0, 20-2-6+2, 20-2-6+2);
        add(anim);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
    }

    @Override
    public void render() {
        super.render();
//        Hitbox hb = getSceneHitbox();
        drawHitbox(SpongeGame.i().getShapeDrawer());
//        SpongeGame.i().getShapeDrawer().rectangle(hb.getLeft() + 5, hb.getBottom(), hb.getWidth(), hb.getHeight());
    }
}
