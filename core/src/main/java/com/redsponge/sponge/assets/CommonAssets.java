package com.redsponge.sponge.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.redsponge.sponge.util.Logger;

public class CommonAssets {

    private static class Constants {
        public static final String PREFIX = "internal/";
        public static final AssetDescriptor<TextureAtlas> LIGHT_ATLAS = new AssetDescriptor<>(PREFIX + "lights/lights.atlas", TextureAtlas.class);
        public static final AssetDescriptor<TextureAtlas> TRANSITION_ATLAS = new AssetDescriptor<>(PREFIX + "transitions/transitions.atlas", TextureAtlas.class);
        public static final String SHADER_PREFIX = PREFIX + "shaders/";
        public static final String[] SHADERS = {
                "passthrough",
                "invert",
                "greyscale",
                "blur_horizontal",
                "blur_vertical"
        };
    }

    private boolean isLoaded;
    private TextureAtlas lightTextures;
    private TextureAtlas transitionTextures;

    private final ShaderLoader shaderLoader;

    public CommonAssets() {
        shaderLoader = new ShaderLoader();
    }

    public TextureAtlas getLightTextures() {
        return lightTextures;
    }

    public TextureAtlas getTransitionTextures() {
        return transitionTextures;
    }

    public ShaderProgram getShader(String shader) {
        return shaderLoader.getShader(shader);
    }

    public void load(AssetManager am) {
        if(isLoaded) {
            Logger.warn(this, "Tried to load already loaded CommonAssets!");
            return;
        }
        Logger.debug(this, "Loading light textures");
        am.load(Constants.LIGHT_ATLAS);
        
        Logger.debug(this, "Loading transition textures");
        am.load(Constants.TRANSITION_ATLAS);
        
        for (String shader : Constants.SHADERS) {
            shaderLoader.load(Constants.SHADER_PREFIX, shader);
        }

        Logger.debug(this, "Loaded Common Assets");
        isLoaded = true;
    }

    public void fillFields(AssetManager am) {
        Logger.debug(this, "Filling fields of CommonAssets");
        
        Logger.debug(this, "Filling lightTextures");
        lightTextures = am.get(Constants.LIGHT_ATLAS);
        
        Logger.debug(this, "Filling transitionTextures");
        transitionTextures = am.get(Constants.TRANSITION_ATLAS);
        // Shaders were already filled.
    }

    public void unload(AssetManager am) {
        if(!isLoaded) {
            Logger.warn(this, "Tried to unload not-loaded CommonAssets!");
            return;
        }
        Logger.debug(this, "Unloading light textures");
        am.unload(Constants.LIGHT_ATLAS.fileName);
        Logger.debug(this, "Unloading ShaderLoader");
        shaderLoader.dispose();
        Logger.debug(this, "Unloaded Common Assets");
        isLoaded = false;

    }
}
