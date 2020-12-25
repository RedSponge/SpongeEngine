package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class ObstacleTile implements RoomTile {

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return false;
    }

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        levelIn.getPlayerVel().scl(-1);
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }

    @Override
    public Color getDebugColor() {
        return Color.GRAY;
    }
}
