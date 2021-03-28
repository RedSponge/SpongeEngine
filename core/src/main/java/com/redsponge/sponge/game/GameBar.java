package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

import java.util.ArrayList;

public class GameBar extends Entity {

    private DrawnComponent barOff, barOn, barDecor;
    private GameTimeManager timeManager;

    public GameBar(Vector2 pos) {
        super(pos);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        TextureAtlas atlas = scene.getAssets().get("ui.atlas");
        barOff = new DrawnComponent(true, true, atlas.findRegion("bar_off"));
        barOn = new DrawnComponent(true, true, atlas.findRegion("bar_on"));
        barDecor = new DrawnComponent(true, true, atlas.findRegion("bar_decor"));

        add(barOff);
        add(barOn);
        add(barDecor);

        barOff.setScaleX(3).setScaleY(3);
        barOn.setScaleX(3).setScaleY(3);
        barDecor.setScaleX(3).setScaleY(3);

        timeManager = getScene().all(new ArrayList<>(), GameTimeManager.class).get(0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float percent = timeManager.getCompletedPercent();
        barOn.setScaleY(percent);
        barOn.setOffsetY(barOn.getRendered().getRegionHeight() * 3 * (1 - percent));
    }

    @Override
    public void render() {
        super.render();
    }
}
