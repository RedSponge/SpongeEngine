package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.redsponge.sponge.SpongeGame;

import java.util.HashMap;

public class Assets {

    private HashMap<String, SceneAssets> sceneAssets = new HashMap<>();
    private AssetMap assetMap;

    private static Assets instance;

    public static Assets get() {
        if(instance == null) instance = new Assets();
        return instance;
    }

    public Assets() {
        assetMap = new AssetMap(Gdx.files.internal("files.txt"));
    }

    public SceneAssets loadScene(String scene) {
        if(sceneAssets.containsKey(scene)) {
            sceneAssets.get(scene).load();
            return sceneAssets.get(scene);
        }
        SceneAssets a = new SceneAssets(SpongeGame.i().getAssetManager(), scene, assetMap);
        a.load();
        sceneAssets.put(scene, a);
        return a;
    }

    public void unloadScene(String scene) {
        if(sceneAssets.containsKey(scene)) {
            sceneAssets.get(scene).unload();
        }
    }
}
