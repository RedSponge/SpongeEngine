package com.redsponge.sponge.game;

import com.redsponge.sponge.screen.Scene;

public class WinScene extends Scene {

    @Override
    public void start() {
        super.start();
    }

    @Override
    public int getWidth() {
        return 480;
    }

    @Override
    public int getHeight() {
        return 270;
    }

    @Override
    public String getName() {
        return "win";
    }
}
