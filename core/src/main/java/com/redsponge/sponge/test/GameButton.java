package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameButton {

    private int x, y;
    private Runnable onClick;
    private int width, height;
    private TextureRegion bg, inner;
    private Color bgColor;

    public GameButton(int x, int y, Runnable onClick, int width, int height, TextureRegion bg, TextureRegion inner) {
        this.x = x;
        this.y = y;
        this.onClick = onClick;
        this.width = width;
        this.height = height;
        this.bg = bg;
        this.inner = inner;
        this.bgColor = Color.WHITE.cpy();
    }

    public void render(SpriteBatch batch) {
        batch.setColor(bgColor);
        batch.draw(bg, x, y, width, height);
        batch.setColor(Color.WHITE);
        if(inner != null) batch.draw(inner, x + width / 2f - inner.getRegionWidth() / 2f, y + height / 2f - inner.getRegionHeight() / 2f);
    }

    public Color getBgColor() {
        return bgColor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public TextureRegion getDrawn() {
        return bg;
    }
}
