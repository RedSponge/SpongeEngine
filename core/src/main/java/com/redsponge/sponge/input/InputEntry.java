package com.redsponge.sponge.input;

import java.util.ArrayList;
import java.util.List;

public class InputEntry {

    private List<SingularInput> inputs;

    public InputEntry() {
        this.inputs = new ArrayList<>();
    }

    public InputEntry addKey(int key) {
        inputs.add(new KeyboardKey(key));
        return this;
    }


    public int isPressedI() {
        return isPressed() ? 1 : 0;
    }

    public boolean isPressed() {
        for (SingularInput input : inputs) {
            if(input.isChecked()) return true;
        }
        return false;
    }

    public int isJustPressedI() {
        return isJustPressed() ? 1 : 0;
    }

    public boolean isJustPressed() {
        for (SingularInput input : inputs) {
            if(input.isJustChecked()) return true;
        }
        return false;
    }

}
