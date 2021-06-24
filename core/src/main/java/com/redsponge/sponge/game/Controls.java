package com.redsponge.sponge.game;

import com.badlogic.gdx.Input.Keys;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;

public class Controls {

    public static final InputEntry JUMP = new InputEntry().addKey(Keys.SPACE);

    public static final InputEntry RIGHT = new InputEntry().addKey(Keys.D).addKey(Keys.RIGHT);
    public static final InputEntry LEFT = new InputEntry().addKey(Keys.A).addKey(Keys.LEFT);

    public static final InputEntry UP = new InputEntry().addKey(Keys.W).addKey(Keys.UP);
    public static final InputEntry DOWN = new InputEntry().addKey(Keys.S).addKey(Keys.DOWN);

    public static final InputAxis HORIZONTAl = new InputAxis(LEFT, RIGHT);
    public static final InputAxis VERTICAL = new InputAxis(DOWN, UP);

    public static final InputEntry ATTACK = new InputEntry().addKey(Keys.X);
}
