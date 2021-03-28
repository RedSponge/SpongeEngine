package com.redsponge.sponge.util;

import com.badlogic.gdx.math.MathUtils;

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

    /**
     * Clamps the absolute value of a number (keeps the sign)
     * @param value The value to clamp, the sign will be kept
     * @param min should be positive
     * @param max should be positive
     * @return a clamped value with the sign kept
     */
    public static float absClamp(float value, float min, float max) {
        int sign = (int) Math.signum(value);
        return sign * clamp(Math.abs(value), min, max);
    }
}
