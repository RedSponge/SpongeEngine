package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class SkipTile extends StallTile {

    @Override
    public void onReleasePlayer(LevelSimulator levelIn) {
        levelIn.getPlayerPos().add(levelIn.getPlayerVel());
        levelIn.progressPlayer();
    }

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public Color getDebugColor() {
        return Color.LIGHT_GRAY;
    }
}
