package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.redsponge.sponge.event.EventBus;

public class SkipTile extends StallTile {

    @Override
    public void onReleasePlayer(LevelSimulator levelIn) {
        EventBus.getInstance().dispatch(new JumpEvent());
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
