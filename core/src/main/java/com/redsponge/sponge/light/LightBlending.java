package com.redsponge.sponge.light;

import com.badlogic.gdx.graphics.GL20;

public enum LightBlending {
    MULTIPLICATIVE(GL20.GL_ZERO, GL20.GL_SRC_COLOR),
    ADDITIVE(GL20.GL_ONE, GL20.GL_ONE),

    ;

    private final int src, dst;

    LightBlending(int src, int dst) {
        this.src = src;
        this.dst = dst;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }
}
