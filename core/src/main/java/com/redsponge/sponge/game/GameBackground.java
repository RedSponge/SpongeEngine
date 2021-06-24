package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class GameBackground extends Entity {

    private DrawnComponent drawnComponent;

    public GameBackground(Vector2 pos) {
        super(pos);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        add(drawnComponent = new DrawnComponent(true, true, scene.getAssets().<TextureAtlas>get("game.atlas").findRegion("background")).setScaleX(2).setScaleY(2));
    }
}
