package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.redsponge.sponge.event.EventBus;

public class WinTile implements RoomTile {

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        EventBus.getInstance().dispatch(new WinEvent());
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }

    @Override
    public Color getDebugColor() {
        return Color.LIME;
    }
}
