package com.redsponge.sponge.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class UVector {

    public static Vector2 toInt(Vector2 v) {
        Vector2 out = new Vector2();
        toInt(v, out);
        return out;
    }

    public static void toInt(Vector2 v, Vector2 out) {
        out.x = Math.round(v.x);
        out.y = Math.round(v.y);
    }

    /**
     * Returns radians
     */
    public static float angleBetween(Vector2 v1, Vector2 v2) {
        return MathUtils.atan2(v2.y - v1.y, v2.x - v1.x);
    }

    public static Vector2 getDirectionVector(Vector2 from, Vector2 to) {
        Vector2 out = new Vector2();
        getDirectionVector(from, to, out);
        return out;
    }

    public static void getDirectionVector(Vector2 from, Vector2 to, Vector2 out) {
        float angle = angleBetween(from, to);
        float dx = MathUtils.cos(angle);
        float dy = MathUtils.sin(angle);

        out.set(dx, dy).nor();
    }

    public static Vector2 perpendicular(Vector2 v) {
        return perpendicular(v, new Vector2(), true);
    }

    public static Vector2 perpendicular(Vector2 v, boolean clockwise) {
        return perpendicular(v, new Vector2(), clockwise);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static Vector2 perpendicular(Vector2 v, Vector2 out, boolean clockwise) {
        if(clockwise) out.set(v.y, -v.x);
        else out.set(-v.y, v.x);
        return out;
    }

}
