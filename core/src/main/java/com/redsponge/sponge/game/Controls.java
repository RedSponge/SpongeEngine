package com.redsponge.sponge.game;

import com.badlogic.gdx.Input.Keys;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;

public class Controls {

    public static final InputEntry JUMP = new InputEntry().addKey(Keys.SPACE);

    public static final InputAxis HORIZONTAl = new InputAxis(new InputEntry().addKey(Keys.A).addKey(Keys.LEFT), new InputEntry().addKey(Keys.D).addKey(Keys.RIGHT));

    public static final InputEntry DOWN = new InputEntry().addKey(Keys.S).addKey(Keys.DOWN);
    public static final InputEntry ATTACK = new InputEntry().addKey(Keys.X);
}
