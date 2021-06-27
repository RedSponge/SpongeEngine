package com.redsponge.sponge.game.win;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class WinBackground extends Entity {


    public WinBackground() {
        super(new Vector2(0, 0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        add(new DrawnComponent(true, true, scene.getAssets().<Texture>get("bg.png")).setScaleX(2).setScaleY(2));
    }
}
