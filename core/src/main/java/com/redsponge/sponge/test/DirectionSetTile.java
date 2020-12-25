package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class DirectionSetTile implements RoomTile {

    private Vector2 dir;

    public DirectionSetTile(Vector2 dir) {
        this.dir = dir;
    }

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        levelIn.getPlayerVel().set(dir);
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }

    @Override
    public Color getDebugColor() {
        return Color.YELLOW;
    }
}
