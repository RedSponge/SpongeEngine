package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.redsponge.sponge.event.EventBus;

public class OrbGiveTile extends SingleUseTile {

    @Override
    public void execute(LevelSimulator levelIn) {
        levelIn.setPlayerProtected(true);
    }

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public Color getDebugColor() {
        return Color.BLUE;
    }

}
