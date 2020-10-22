package com.redsponge.sponge.renering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class RenderingEffect {

    private boolean isActive;

    public RenderingEffect(boolean isActive) {
        this.isActive = isActive;
    }

    abstract void apply(FitViewport viewport, SpriteBatch batch, TextureRegion buffer);

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
