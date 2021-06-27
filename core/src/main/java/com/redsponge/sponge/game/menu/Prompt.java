package com.redsponge.sponge.game.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class Prompt extends Entity {

    private DrawnComponent drawn;
    private float time;

    public Prompt(Vector2 pos) {
        super(pos);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        drawn = new DrawnComponent(true, true, scene.getAssets().<Texture>get("prompt.png"));
        add(drawn);
        drawn.setScaleY(4).setScaleX(4);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;


        float scl = 4 + MathUtils.sin(time) * 0.2f;
        drawn.setScaleX(scl).setScaleY(scl);
        drawn.setOriginX(drawn.getRendered().getRegionWidth() / 2f).setOriginY(drawn.getRendered().getRegionHeight() / 2f);

    }

    @Override
    public void render() {
        super.render();
    }
}
