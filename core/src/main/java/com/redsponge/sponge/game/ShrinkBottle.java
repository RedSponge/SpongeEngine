package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class ShrinkBottle extends Entity {

    private DrawnComponent drawn;
    private float time;
    private int bloomRenderId;

    public ShrinkBottle(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 10, 29);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("shrink_bottle"));
        add(drawn);
        drawn.setOffsetX(-33 * drawn.getScaleX());

        bloomRenderId = ((GameScene)scene).getBloomEffect().addBloomRender(() -> {
            drawn.getColor().a = 0.5f;
            drawn.render();
            drawn.getColor().a = 1;
        });
    }

    @Override
    public void update(float delta) {
        getPositionf().y += delta * BackgroundRenderer.bgScrollSpeed;
        super.update(delta);

        time += delta;

        float scl = 1 + MathUtils.sin(time * 5) * 0.1f;
        drawn.setScaleX(scl).setScaleY(scl);

        if(getYf() > getScene().getHeight()) {
            removeSelf();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void removed() {
        ((GameScene)getScene()).getBloomEffect().removeRender(bloomRenderId);

        super.removed();
    }
}
