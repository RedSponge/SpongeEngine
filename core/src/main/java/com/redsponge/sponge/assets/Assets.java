package com.redsponge.sponge.assets;

import com.redsponge.sponge.SpongeGame;

import java.util.HashMap;

public class Assets {

    private static HashMap<String, SceneAssets> sceneAssets = new HashMap<>();

    public static SceneAssets loadScene(String scene) {
        if(sceneAssets.containsKey(scene)) {
            sceneAssets.get(scene).load();
            return sceneAssets.get(scene);
        }
        SceneAssets a = new SceneAssets(SpongeGame.i().getAssetManager(), scene);
        a.load();
        sceneAssets.put(scene, a);
        return a;
    }

    public static void unloadScene(String scene) {
        if(sceneAssets.containsKey(scene)) {
            sceneAssets.get(scene).unload();
        }
    }
}
