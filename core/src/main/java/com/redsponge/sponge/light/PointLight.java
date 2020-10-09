package com.redsponge.sponge.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PointLight implements Light {

    private Vector2 pos;
    private TextureRegion texture;
    private float radius;
    private final Color color;

    public PointLight(float x, float y, float radius, TextureRegion texture) {
        this.pos = new Vector2(x, y);
        this.radius = radius;
        this.texture = texture;
        this.color = Color.WHITE.cpy();
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
        batch.setColor(color);
        batch.draw(texture, pos.x - radius / 2f, pos.y - radius / 2f, radius, radius);
    }

    public float getRadius() {
        return radius;
    }

    public PointLight setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public Color getColor() {
        return color;
    }
}
