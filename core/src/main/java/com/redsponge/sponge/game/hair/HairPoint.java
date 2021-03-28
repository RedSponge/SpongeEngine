package com.redsponge.sponge.game.hair;

import com.badlogic.gdx.math.Vector2;

public class HairPoint {

    public Vector2 pos;
    public Vector2 offset;
    public float maxDistance;
    public float radius;

    private final Vector2 tmp;
    private final Vector2 tmp2;

    public HairPoint(Vector2 offset, float maxDistance, float radius) {
        this.pos = new Vector2();
        this.offset = offset;
        this.maxDistance = maxDistance;
        this.radius = radius;

        tmp = new Vector2();
        tmp2 = new Vector2();
    }

    public Vector2 getOffsettedPosition(boolean flipped, float scale) {
        return tmp.set(pos).add(offset.x * (flipped ? -1 : 1) * scale, offset.y * scale);
    }
}
