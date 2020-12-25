package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class EmptyTile implements RoomTile {
    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        return;
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {
        return;
    }

    @Override
    public Color getDebugColor() {
        return Color.WHITE;
    }
}
