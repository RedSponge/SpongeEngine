package com.redsponge.sponge.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class CommonAssets {

    private static class Constants {
        public static final String PREFIX = "__internal/";
        public static final AssetDescriptor<TextureAtlas> LIGHT_ATLAS = new AssetDescriptor<>(PREFIX + "lights/lights.atlas", TextureAtlas.class);
    }

    private boolean isLoaded;
    private TextureAtlas lightTextures;

    public TextureAtlas getLightTextures() {
        return lightTextures;
    }




    public void load(AssetManager am) {
        if(isLoaded) {

        }
        am.load(Constants.LIGHT_ATLAS);
    }

    public void fillFields(AssetManager am) {
        lightTextures = am.get(Constants.LIGHT_ATLAS);
    }

    public void unload(AssetManager am) {
        am.unload(Constants.LIGHT_ATLAS.fileName);
    }
}
