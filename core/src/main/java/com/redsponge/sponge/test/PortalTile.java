package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class PortalTile extends StallTile {
    private PortalTile other;

    public PortalTile() {
        super();
    }

    @Override
    public void onReleasePlayer(LevelSimulator levelIn) {
        levelIn.getPlayerPos().set(levelIn.findTile(other));
    }

    public PortalTile(PortalTile other) {
        this();
        this.other = other;
    }

    @Override
    public Color getDebugColor() {
        return Color.CYAN;
    }

    public void setOther(PortalTile other) {
        this.other = other;
    }

    public PortalTile getOther() {
        return other;
    }
}
