package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Vector2;

public abstract class StallTile implements RoomTile {

    private boolean holdingPlayer;
    private Vector2 playerVel;

    public StallTile() {
        playerVel = new Vector2();
        holdingPlayer = false;
    }

    @Override
    public boolean canPlayerEnter(LevelSimulator levelIn) {
        return true;
    }

    public abstract void onReleasePlayer(LevelSimulator levelIn);

    @Override
    public void onPlayerEnter(LevelSimulator levelIn) {
        if(!holdingPlayer) {
            playerVel.set(levelIn.getPlayerVel());
            levelIn.getPlayerVel().set(0, 0);
            holdingPlayer = true;
        } else {
            levelIn.getPlayerVel().set(playerVel);
            holdingPlayer = false;
            onReleasePlayer(levelIn);
        }
    }

    @Override
    public void onPlayerPreEnter(LevelSimulator levelIn) {

    }
}
