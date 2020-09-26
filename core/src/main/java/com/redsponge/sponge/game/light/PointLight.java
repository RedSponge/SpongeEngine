package com.redsponge.sponge.game.light;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PointLight implements Light {

    private float x, y, w, h;
    private TextureRegion region;

    public PointLight(float x, float y, float w, float h, TextureRegion region) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.region = region;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region, x, y, w, h);
    }
}
