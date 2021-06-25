package com.redsponge.sponge.game;

import com.badlogic.gdx.Input.Keys;
import com.redsponge.sponge.input.InputAxis;
import com.redsponge.sponge.input.InputEntry;

public class PlayerControls {

    private final InputAxis horizontal, vertical;
    private final InputEntry right, up, left, down, jump;
    private final InputEntry attack;

    public PlayerControls(int left, int right, int up, int down, int jump, int attack) {
        this.right = new InputEntry().addKey(right);
        this.left = new InputEntry().addKey(left);
        this.up = new InputEntry().addKey(up);
        this.down = new InputEntry().addKey(down);
        this.attack = new InputEntry().addKey(attack);
        this.jump = new InputEntry().addKey(jump);

        this.horizontal = new InputAxis(this.left, this.right);
        this.vertical = new InputAxis(this.down, this.up);
    }

    public InputAxis getHorizontal() {
        return horizontal;
    }

    public InputAxis getVertical() {
        return vertical;
    }

    public InputEntry getJump() {
        return jump;
    }

    public InputEntry getAttack() {
        return attack;
    }

    public InputEntry getRight() {
        return right;
    }

    public InputEntry getUp() {
        return up;
    }

    public InputEntry getLeft() {
        return left;
    }

    public InputEntry getDown() {
        return down;
    }
}
