package com.redsponge.sponge.event;

import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;

public class CollisionEvent {

    private PActor collider;
    private Collision collision;

    public CollisionEvent(PActor collider, Collision collision) {
        this.collider = collider;
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

    public PActor getCollider() {
        return collider;
    }

    public CollisionEvent setCollider(PActor collider) {
        this.collider = collider;
        return this;
    }
}
