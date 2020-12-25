package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class DeathTile implements RoomTile {
    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        if(levelIn.isPlayerProtected()) levelIn.setPlayerProtected(false);
        else levelIn.reset();
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }

    @Override
    public Color getDebugColor() {
        return Color.RED;
    }
}
