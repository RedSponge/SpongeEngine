package com.redsponge.sponge.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.redsponge.sponge.util.Logger;

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
            Logger.warn(this, "Tried to load already loaded CommonAssets!");
            return;
        }
        am.load(Constants.LIGHT_ATLAS);
        isLoaded = true;
        
        Logger.debug(this, "Loaded Common Assets");
    }

    public void fillFields(AssetManager am) {
        Logger.debug(this, "Filling fields of CommonAssets");
        lightTextures = am.get(Constants.LIGHT_ATLAS);
    }

    public void unload(AssetManager am) {
        if(!isLoaded) {
            Logger.warn(this, "Tried to unload not-loaded CommonAssets!");
            return;
        }
        am.unload(Constants.LIGHT_ATLAS.fileName);
        isLoaded = false;

        Logger.debug(this, "Unloaded Common Assets");
    }
}
