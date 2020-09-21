package com.redsponge.sponge.physics;

public class Trigger {
    public PTrigger trigger;
    public boolean isEnter;

    public Trigger(PTrigger trigger, boolean isEnter) {
        this.trigger = trigger;
        this.isEnter = isEnter;
    }
}
