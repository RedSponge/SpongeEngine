package com.redsponge.sponge.test;

import com.badlogic.gdx.Input.Keys;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;
import sun.awt.AWTAccessor.InputEventAccessor;

public class Controls {

    public static final InputEntry JUMP = new InputEntry().addKey(Keys.SPACE);

    public static final InputAxis HORIZONTAl = new InputAxis(new InputEntry().addKey(Keys.A), new InputEntry().addKey(Keys.D));

    public static final InputEntry DOWN = new InputEntry().addKey(Keys.S);
}
