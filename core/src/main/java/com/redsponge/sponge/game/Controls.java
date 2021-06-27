package com.redsponge.sponge.game;

import com.badlogic.gdx.Input;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;

public class Controls {

    public static final InputAxis HORIZONTAL = new InputAxis(new InputEntry().addKey(Input.Keys.LEFT), new InputEntry().addKey(Input.Keys.RIGHT));
    public static final InputEntry GLIDE = new InputEntry().addKey(Input.Keys.SPACE).addKey(Input.Keys.X);
}
