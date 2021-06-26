package com.redsponge.sponge.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {

    public static void main(String[] args) {
        TexturePacker.processIfModified("raw/torch", "../assets/test/texture", "torch");
        TexturePacker.processIfModified("raw/game", "../assets/game/texture", "game");
        TexturePacker.processIfModified("raw/dashni", "../assets/game/texture", "dashni");
    }

}
