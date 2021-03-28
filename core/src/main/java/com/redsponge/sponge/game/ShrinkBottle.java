package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class ShrinkBottle extends Entity {

    private DrawnComponent drawn;

    public ShrinkBottle(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 10 * 2, 29 * 2);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("shrink_bottle"));

        drawn.setScaleX(2).setScaleY(2);
        add(drawn);
        drawn.setOffsetX(-33 * drawn.getScaleX());
    }

    @Override
    public void update(float delta) {
        getPositionf().y += delta * BackgroundRenderer.bgScrollSpeed;
        super.update(delta);
    }

    @Override
    public void render() {
        super.render();
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }
}
