package com.redsponge.sponge.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {

    public static void main(String[] args) {
        TexturePacker.processIfModified("texture-packer/raw/player", "assets/game/texture", "player");
        TexturePacker.processIfModified("texture-packer/raw/particles", "assets/game/particle", "textures");
    }

}
