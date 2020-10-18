package com.redsponge.sponge.util;

public final class UMath {

    public static float approach(float value, float target, float step) {
        return value > target ? Math.max(value - step, target) : Math.min(value + step, target);
    }

    public static float lerp(float value, float target, float alpha) {
        return (1 - alpha) * value + alpha * target;
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }
}
