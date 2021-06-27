package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class GrowCake extends Entity {

    private DrawnComponent drawn;
    private float time;
    private int bloomRenderId;

    public GrowCake(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 30, 23);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("grow_cake"));
        add(drawn);

        bloomRenderId = ((GameScene)scene).getBloomEffect().addBloomRender(() -> {
            drawn.getColor().a = 0.5f;
            drawn.render();
            drawn.getColor().a = 1;
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
//        drawn.setOffsetY((float) (Math.sin(time) * 5));
        getPositionf().y += BackgroundRenderer.bgScrollSpeed * delta;
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
