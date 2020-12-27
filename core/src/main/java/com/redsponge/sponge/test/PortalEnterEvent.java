package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class PortalEnterEvent {

    public final Color color;
    public final Vector2 position;

    public PortalEnterEvent(Color color, Vector2 position) {
        this.color = color;
        this.position = position;
    }
}
