package com.redsponge.sponge.input;

import com.badlogic.gdx.Gdx;

public class KeyboardKey implements SingularInput {

    private int key;

    public KeyboardKey(int key) {
        this.key = key;
    }

    @Override
    public boolean isChecked() {
        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean isJustChecked() {
        return Gdx.input.isKeyJustPressed(key);
    }
}
