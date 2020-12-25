package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public class PortalTile extends StallTile {
    private PortalTile other;

    public static final Color[] colours = new Color[] {
            Color.CYAN, Color.PURPLE, Color.MAGENTA, Color.MAROON
    };

    public PortalTile(Color colour) {
        super();
        this.colour = colour;
    }

    @Override
    public void onReleasePlayer(LevelSimulator levelIn) {
        levelIn.getPlayerPos().set(levelIn.findTile(other));
    }

    private Color colour;

    public PortalTile(Color colour, PortalTile other) {
        this(colour);
        this.other = other;
    }

    @Override
    public Color getDebugColor() {
        return colour;
    }

    public void setOther(PortalTile other) {
        this.other = other;
    }

    public PortalTile getOther() {
        return other;
    }
}
