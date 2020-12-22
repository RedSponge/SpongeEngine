package com.redsponge.sponge.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {

    public static void main(String[] args) {
        TexturePacker.processIfModified("raw/torch", "../assets/test/texture", "torch");
        TexturePacker.processIfModified("raw/player", "../assets/test/texture", "knight");
        TexturePacker.processIfModified("raw/enemy", "../assets/test/texture", "enemy");
    }

}
