package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class GrowCake extends Entity {

    private DrawnComponent drawn;
    private float time;

    public GrowCake(Vector2 pos) {
        super(pos);
        getHitbox().set(0, 0, 30*2, 23*2);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("grow_cake"));
        drawn.setScaleX(2).setScaleY(2);
        add(drawn);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
//        time += delta;
//        drawn.setOffsetY((float) (Math.sin(time) * 5));
        getPositionf().y += BackgroundRenderer.bgScrollSpeed * delta;
    }

    @Override
    public void render() {
        super.render();
        drawHitbox(SpongeGame.i().getShapeDrawer());
    }
}
