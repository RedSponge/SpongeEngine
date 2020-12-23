package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;

public class Enemy extends PActor {

    private AnimationComponent drawn;
    private AnimationNodeSystem system;

    public Enemy(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 16, 30);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        system = scene.getAssets().getAnimationNodeSystemInstance("enemy");
        add(drawn = new AnimationComponent(true, true, system.getActiveAnimation()));
        drawn.setOffsetX(-20);
        drawn.setOffsetY(-8);
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getShapeDrawer().rectangle(getSceneHitbox().getRectangle());
    }
}
