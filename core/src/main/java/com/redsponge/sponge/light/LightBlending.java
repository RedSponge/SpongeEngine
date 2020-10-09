package com.redsponge.sponge.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;


public enum LightBlending {
    MULTIPLICATIVE(GL20.GL_ZERO, GL20.GL_SRC_COLOR, Color.WHITE.cpy()),
    ADDITIVE(GL20.GL_ONE, GL20.GL_ONE, new Color(0, 0, 0, 0)),

    ;

    private final int src, dst;
    private final Color defaultColor;

    LightBlending(int src, int dst, Color defaultColor) {
        this.src = src;
        this.dst = dst;
        this.defaultColor = defaultColor;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }
}
