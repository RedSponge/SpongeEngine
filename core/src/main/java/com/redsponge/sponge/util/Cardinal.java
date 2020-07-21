package com.redsponge.sponge.util;

import com.badlogic.gdx.math.Vector2;

public enum Cardinal {

    Right,
    Left,
    Up,
    Down;

    public int getX() {
        switch (this) {
            case Right:
                return 1;
            case Left:
                return -1;
            default:
                return 0;
        }
    }

    public int getY() {
        switch (this) {
            case Up:
                return 1;
            case Down:
                return -1;
            default:
                return 0;
        }
    }

    public Cardinal getOpposite() {
        switch (this) {
            case Right:
                return Left;
            case Left:
                return Right;
            case Up:
                return Down;
            case Down:
                return Up;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Cardinal getNextClockwise() {
        switch (this) {
            case Right:
                return Down;
            case Down:
                return Left;
            case Left:
                return Up;
            case Up:
                return Right;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Cardinal getNextCounterClockwise() {
        switch (this) {
            case Right:
                return Up;
            case Up:
                return Left;
            case Left:
                return Down;
            case Down:
                return Right;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Cardinal fromFacing(Facing f) {
        return f == Facing.Right ? Right : Left;
    }

    public Vector2 toVector() {
        switch (this) {
            case Right:
                return Vector2.X.cpy();
            case Left:
                return Vector2.X.cpy().scl(-1);
            case Up:
                return Vector2.Y.cpy();
            case Down:
                return Vector2.Y.cpy().scl(-1);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Cardinal fromVector(Vector2 vec) {
        boolean hasX = vec.x != 0, hasY = vec.y != 0;
        if(hasX == hasY) throw new RuntimeException("Couldn't turn vector " + vec + " to cardinal!");
        if(vec.x > 0) return Right;
        if(vec.x < 0) return Left;
        if(vec.y > 0) return Up;
        if(vec.y < 0) return Down;
        throw new RuntimeException("Unreachable Cardinal Code"); // Unreachable
    }

}
