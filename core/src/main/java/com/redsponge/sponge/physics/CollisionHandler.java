package com.redsponge.sponge.physics;

@FunctionalInterface
public interface CollisionHandler {
    void onCollision(Collision collision);
}
