package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Vector2;

public abstract class SingleUseTile implements RoomTile {

    public abstract void execute(LevelSimulator levelIn);

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        execute(levelIn);
        Vector2 selfPos = levelIn.findTile(this);
        levelIn.setRoomObject(new EmptyTile(), (int) selfPos.x, (int) selfPos.y);
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }
}
