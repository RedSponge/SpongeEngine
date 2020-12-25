package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;

public interface RoomTile {

    /**
     * @return Can the player enter this tile?
     */
    boolean canPlayerEnter(LevelSimulator levelIn);

    /**
     * When the player goes to this tile
     */
    void onPlayerEnter(LevelSimulator levelIn);

    /**
     * When the player will go to this tile next turn
     */
    void onPlayerPreEnter(LevelSimulator levelIn);

    Color getDebugColor();
}
