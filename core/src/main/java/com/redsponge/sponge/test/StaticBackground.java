package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.test.GameScene.WorldMode;

public class StaticBackground extends Entity {

    private Color colour;

    public StaticBackground() {
        super(new Vector2(0, 0));
        colour = Color.DARK_GRAY;
    }

    @Override
    public void render() {
        super.render();
        colour = ((GameScene)getScene()).getMode() == WorldMode.FIRE ? Color.ORANGE : Color.BLUE;
        int x = (int) getScene().viewport.getCamera().position.x - getScene().getWidth() / 2;
        int y = (int) getScene().viewport.getCamera().position.y - getScene().getHeight() / 2;
        SpongeGame.i().getShapeDrawer().filledRectangle(x, y, getScene().getWidth(), getScene().getHeight(), colour);
    }
}
