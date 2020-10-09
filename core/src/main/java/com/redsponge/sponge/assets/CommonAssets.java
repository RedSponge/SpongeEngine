package com.redsponge.sponge.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.util.Logger;

import java.util.HashMap;

public class CommonAssets {

    private static class Constants {
        public static final String PREFIX = "__internal/";
        public static final AssetDescriptor<TextureAtlas> LIGHT_ATLAS = new AssetDescriptor<>(PREFIX + "lights/lights.atlas", TextureAtlas.class);

        public static final String SHADER_PREFIX = PREFIX + "shaders/";
        public static final String[] SHADERS = {
                "passthrough",
                "invert",
                "greyscale"
        };
    }

    private boolean isLoaded;
    private TextureAtlas lightTextures;

    private final HashMap<String, ShaderProgram> shaders;

    public CommonAssets() {
        shaders = new HashMap<>();
    }

    public TextureAtlas getLightTextures() {
        return lightTextures;
    }
    public ShaderProgram getShader(String shader) {
        return shaders.get(shader);
    }

    public void load(AssetManager am) {
        if(isLoaded) {
            Logger.warn(this, "Tried to load already loaded CommonAssets!");
            return;
        }
        Logger.debug(this, "Loading light textures");
        am.load(Constants.LIGHT_ATLAS);

        for (String shader : Constants.SHADERS) {
            Logger.debug(this, "Loading shader", shader);
            am.load(Constants.SHADER_PREFIX + shader + ".vert", ShaderProgram.class);
            am.load(Constants.SHADER_PREFIX + shader + ".frag", ShaderProgram.class);
        }

        Logger.debug(this, "Loaded Common Assets");
        isLoaded = true;
    }

    public void fillFields(AssetManager am) {
        Logger.debug(this, "Filling fields of CommonAssets");
        lightTextures = am.get(Constants.LIGHT_ATLAS);

        for (String shader : Constants.SHADERS) {
            ShaderProgram prog = am.get(Constants.SHADER_PREFIX + shader + ".vert");
            if(!prog.isCompiled()) {
                Logger.error(this, "Failed to compile shader!", prog.getLog());
            } else {
                shaders.put(shader, am.get(Constants.SHADER_PREFIX + shader + ".vert"));
            }
        }
    }

    public void unload(AssetManager am) {
        if(!isLoaded) {
            Logger.warn(this, "Tried to unload not-loaded CommonAssets!");
            return;
        }
        Logger.debug(this, "Unloading light textures");
        am.unload(Constants.LIGHT_ATLAS.fileName);
        for (String shader : Constants.SHADERS) {
            Logger.debug(this, "Unloading shader", shader);
            am.unload(Constants.SHADER_PREFIX + shader + ".vert");
            am.unload(Constants.SHADER_PREFIX + shader + ".frag");
        }


        Logger.debug(this, "Unloaded Common Assets");
        isLoaded = false;
    }
}
