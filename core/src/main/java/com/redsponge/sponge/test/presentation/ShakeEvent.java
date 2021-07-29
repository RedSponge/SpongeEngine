package com.redsponge.sponge.test.presentation;

public class ShakeEvent {
    public final float time;
    public final boolean override;

    public ShakeEvent(float time, boolean override) {
        this.time = time;
        this.override = override;
    }
}
