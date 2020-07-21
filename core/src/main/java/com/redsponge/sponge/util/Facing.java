package com.redsponge.sponge.util;

public enum Facing {

    Right(1),
    Left(-1);

    private int dir;

    Facing(int dir) {
        this.dir = dir;
    }

    public Facing getOpposite() {
        switch (this) {
            case Right:
                return Left;
            case Left:
                return Right;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Facing fromInt(int dir) {
        return fromInt(dir, Right);
    }

    public static Facing fromInt(int dir, Facing onZero) {
        return dir == 0 ? onZero : dir > 0 ? Right : Left;
    }
}
