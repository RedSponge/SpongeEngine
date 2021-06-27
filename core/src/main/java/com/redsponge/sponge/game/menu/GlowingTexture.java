package com.redsponge.sponge.game.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class GlowingTexture extends Entity {

    private DrawnComponent drawn;
    private String tex;
    private float time;

    public GlowingTexture(Vector2 pos, String tex) {
        super(pos);
        this.tex = tex;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        add(drawn = new DrawnComponent(true, true, scene.getAssets().<Texture>get(tex)));
    }

    @Override
    public void update(float delta) {
        this.time += delta;

        float scl = 2 + MathUtils.sin(time) * 0.2f;
        drawn.setScaleX(scl).setScaleY(scl);
        drawn.setOriginX(drawn.getRendered().getRegionWidth() / 2f).setOriginY(drawn.getRendered().getRegionHeight() / 2f);
        super.update(delta);
    }
}
