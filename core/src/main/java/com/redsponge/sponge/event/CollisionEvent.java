package com.redsponge.sponge.event;

import com.redsponge.sponge.physics.Collision;

public class CollisionEvent {

    private Collision collision;

    public CollisionEvent(Collision collision) {
        this.collision = collision;
    }

    public Collision getCollision() {
        return collision;
    }

    public CollisionEvent setCollision(Collision collision) {
        this.collision = collision;
        return this;
    }

    @Override
    public String toString() {
        return "CollisionEvent{" +
                "collision=" + collision +
                '}';
    }
}
