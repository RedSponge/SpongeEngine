package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class BackgroundRenderer extends Entity {

    private Texture background;
    private float offsetY;

    public static final float bgScrollSpeed = 200;

    public BackgroundRenderer() {
        super(new Vector2(0,0));
        offsetY = -640;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        background = scene.getAssets().get("background.png");
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        offsetY += bgScrollSpeed * delta;
    }

    @Override
    public void render() {
        super.render();
        float scale = (float) getScene().getWidth() / background.getWidth();

        float width = getScene().getWidth();
        float height = background.getHeight() * scale;

        SpongeGame.i().getBatch().draw(background, 0, offsetY % height, width, height);
        SpongeGame.i().getBatch().draw(background, 0, (offsetY % height) - height, width, height);
    }

    @Override
    public int getzIndex() {
        return -1;
    }
}
