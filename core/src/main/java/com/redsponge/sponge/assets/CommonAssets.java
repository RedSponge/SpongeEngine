package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.redsponge.sponge.util.Logger;

import java.util.HashMap;

public class CommonAssets {

    private static class Constants {
        public static final String PREFIX = "internal/";
        public static final AssetDescriptor<TextureAtlas> LIGHT_ATLAS = new AssetDescriptor<>(PREFIX + "lights/lights.atlas", TextureAtlas.class);
        public static final String TRANSITIONS_FOLDER = PREFIX + "transitions";
        public static final String SHADER_PREFIX = PREFIX + "shaders/";
        public static final String[] SHADERS = {
                "passthrough",
                "invert",
                "greyscale",
                "blur_horizontal",
                "blur_vertical",
                "transition",
                "contrast",
                "pixelate"
        };
    }

    private boolean isLoaded;
    private TextureAtlas lightTextures;

    private final ShaderLoader shaderLoader;
    private final AssetMap assetMap;
    private final HashMap<String, Texture> transitionTextures;

    public CommonAssets(AssetMap assetMap) {
        this.assetMap = assetMap;
        this.shaderLoader = new ShaderLoader();
        this.transitionTextures = new HashMap<>();
    }

    public TextureAtlas getLightTextures() {
        return lightTextures;
    }

    public ShaderProgram getShader(String shader) {
        return shaderLoader.getShader(shader);
    }

    public Texture getTransitionTexture(String transitionTexture) {
        return transitionTextures.get(transitionTexture);
    }

    public void load(AssetManager am) {
        if(isLoaded) {
            Logger.warn(this, "Tried to load already loaded CommonAssets!");
            return;
        }
        Logger.debug(this, "Loading light textures");
        am.load(Constants.LIGHT_ATLAS);
        
        Logger.debug(this, "Loading transition textures");
        FileHandle[] handles = assetMap.list(Gdx.files.internal(Constants.TRANSITIONS_FOLDER));
        for (int i = 0; i < handles.length; i++) {
            am.load(handles[i].path(), Texture.class);
        }

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

        Logger.debug(this, "Filling transition textures");

        FileHandle[] handles = assetMap.list(Gdx.files.internal(Constants.TRANSITIONS_FOLDER));
        for (int i = 0; i < handles.length; i++) {
            transitionTextures.put(handles[i].name(), am.get(handles[i].path(), Texture.class));
        }
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
