package com.redsponge.sponge.game.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class MenuBackground extends Entity {

    private DrawnComponent drawn;

    public MenuBackground() {
        super(new Vector2(0, 0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        add(drawn = new DrawnComponent(true, true, scene.getAssets().<Texture>get("bg.png")));
        drawn.setScaleX(4).setScaleY(4);;
    }
}
