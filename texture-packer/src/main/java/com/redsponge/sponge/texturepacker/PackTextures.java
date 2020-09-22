package com.redsponge.sponge.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {

    public static void main(String[] args) {
        TexturePacker.processIfModified("../texture-packer/raw/player", "game/texture", "player");
        TexturePacker.processIfModified("../texture-packer/raw/particles", "game/particle", "textures");
        TexturePacker.processIfModified("../texture-packer/raw/world", "game/texture", "world");
    }

}
