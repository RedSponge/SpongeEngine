package com.redsponge.sponge.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {

    public static void main(String[] args) {
//        TexturePacker.processIfModified("raw/transitions", "../assets/internal/transitions", "transitions");
        TexturePacker.processIfModified("raw/torch", "../assets/test/texture", "torch");
        TexturePacker.processIfModified("raw/player", "../assets/game/texture", "player");
        TexturePacker.processIfModified("raw/world", "../assets/game/texture", "world");
    }

}
