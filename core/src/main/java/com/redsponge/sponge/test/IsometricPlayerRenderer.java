package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Logger;

public class IsometricPlayerRenderer extends Entity {

    private Vector2 vel;

    private Texture upTex, downTex;

    private DrawnComponent drawn;
    private Vector2 referencePos, referenceVel;
    private Vector2 requiredByReference;
    private IsometricTileMapRenderer mapRenderer;

    public IsometricPlayerRenderer(Vector2 pos, Vector2 refPos, Vector2 refVel) {
        super(pos);
        vel = new Vector2();
        this.referencePos = refPos;
        this.referenceVel = refVel;
        this.requiredByReference = new Vector2();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        upTex = scene.getAssets().get("player_up.png");
        downTex = scene.getAssets().get("player_down.png");

        drawn = new DrawnComponent(true, true, upTex);
        add(drawn);
        mapRenderer = scene.first(IsometricTileMapRenderer.class);
    }

    @Override
    public void update(float delta) {
        mapRenderer.getPositionOfIndex((int) referencePos.x, (int) referencePos.y, requiredByReference);
        setX(requiredByReference.x + 14);
        setY(requiredByReference.y + 7);
        drawn.setOffsetX(-4);
        drawn.setOffsetY(-4);

        if(referenceVel.x != 0 || referenceVel.y != 0) {
            boolean up = referenceVel.x + referenceVel.y > 0;
            boolean flipped = referenceVel.y > 0 || referenceVel.x < 0;
            Logger.warn(this, (int) referenceVel.x, (int) referenceVel.y, up, flipped);
            drawn.getRendered().setTexture(up ? upTex : downTex);
            drawn.setFlippedX(flipped);
        }
        drawn.getColor().set(mapRenderer.getLevel().isPlayerProtected() ? Color.BLUE : Color.WHITE);

        super.update(delta);
    }
}
