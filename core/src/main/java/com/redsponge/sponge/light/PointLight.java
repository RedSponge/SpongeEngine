package com.redsponge.sponge.light;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PointLight implements Light {

    private Vector2 pos;
    private TextureRegion texture;

    public PointLight(float x, float y, TextureRegion texture) {
        pos = new Vector2(x, y);
        this.texture = texture;
    }

    public Vector2 getPosition() {
        return pos;
    }

    public PointLight setPosition(Vector2 pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, pos.x - texture.getRegionWidth() / 2f, pos.y - texture.getRegionHeight());
    }
}
