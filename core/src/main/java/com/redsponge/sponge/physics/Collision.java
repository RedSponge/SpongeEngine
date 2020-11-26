package com.redsponge.sponge.physics;

import com.badlogic.gdx.math.Vector2;

public class Collision {

    public Vector2 dir;
    public int mag;
    public int completed;
    public WorldGeometry stopper;
    public WorldGeometry pusher;

    public Collision(Vector2 dir, int mag, int completed, WorldGeometry stopper, WorldGeometry pusher) {
        this.dir = dir;
        this.mag = mag;
        this.completed = completed;
        this.stopper = stopper;
        this.pusher = pusher;
    }

    @Override
    public String toString() {
        return "Collision{" +
                "dir=" + dir +
                ", mag=" + mag +
                ", completed=" + completed +
                ", stopper=" + stopper +
                ", pusher=" + pusher +
                '}';
    }
}
