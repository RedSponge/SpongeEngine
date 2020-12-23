package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class StaticBackground extends Entity {

    private Color colour;
    private Texture bg;
    private DrawnComponent drawn;

    public StaticBackground() {
        super(new Vector2(0, 0));
        colour = new Color(0.5f, 0.8f, 1, 1);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        bg = scene.getAssets().get("bg.png");
        add(drawn = new DrawnComponent(true, true, bg));
        setY(100);
        drawn.setScaleX(3).setScaleY(3).setOffsetY(-100);
    }

    @Override
    public void render() {
        super.render();
//        int y = (int) getScene().getViewport().getCamera().position.y - getScene().getHeight() / 2;
//        int x = (int) getScene().getViewport().getCamera().position.x - getScene().getWidth() / 2;
//        SpongeGame.i().getShapeDrawer().filledRectangle(x, y, getScene().getWidth(), getScene().getHeight(), colour);
    }
}
