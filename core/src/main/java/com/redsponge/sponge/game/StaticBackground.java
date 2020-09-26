package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.game.GameScene.WorldMode;
import com.redsponge.sponge.screen.Scene;

public class StaticBackground extends Entity {

    private Color colour;

    public static final Color hotColour = new Color(0.5f, 0.2f, 0.1f, 1.0f);
    public static final Color coldColour = new Color(0.1f, 0.2f, 0.5f, 1.0f);

    private TextureRegion ice, fire;

    public StaticBackground() {
        super(new Vector2(0, 0));
        colour = Color.DARK_GRAY;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        ice = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("ice_world");
        fire = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("fire_world");
    }

    @Override
    public void render() {
        super.render();
        colour = ((GameScene)getScene()).getMode() == WorldMode.FIRE ? hotColour : coldColour;
        int x = (int) getScene().viewport.getCamera().position.x - getScene().getWidth() / 2;
        int y = (int) getScene().viewport.getCamera().position.y - getScene().getHeight() / 2;
        TextureRegion reg = ((GameScene)getScene()).getMode() == WorldMode.FIRE ? fire : ice;
        SpongeGame.i().getBatch().draw(reg, 0, 0, reg.getRegionWidth() * 4, reg.getRegionHeight() * 4);
//        SpongeGame.i().getShapeDrawer().filledRectangle(x, y, getScene().getWidth(), getScene().getHeight(), colour);
    }
}
