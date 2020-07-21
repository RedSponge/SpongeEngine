package com.redsponge.sponge.input;

public class InputAxis {

    private InputEntry positive, negative;

    public InputAxis(InputEntry negative, InputEntry positive) {
        this.negative = negative;
        this.positive = positive;
    }

    public int get() {
        return positive.isPressedI() - negative.isPressedI();
    }

    public InputEntry getPositive() {
        return positive;
    }

    public InputEntry getNegative() {
        return negative;
    }
}
