package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.SpongeGame;

import java.util.HashMap;

public class Assets implements Disposable {

    private HashMap<String, SceneAssets> sceneAssets = new HashMap<>();
    private AssetMap assetMap;
    private CommonAssets commonAssets;

    private static Assets instance;

    public static Assets get() {
        if(instance == null) instance = new Assets();
        return instance;
    }

    public Assets() {
        assetMap = new AssetMap(Gdx.files.internal("files.txt"));
        commonAssets = new CommonAssets(assetMap);
        commonAssets.load(SpongeGame.i().getAssetManager());
        SpongeGame.i().getAssetManager().finishLoading();
        commonAssets.fillFields(SpongeGame.i().getAssetManager());
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

    public void dispose() {
        commonAssets.unload(SpongeGame.i().getAssetManager());
    }

    public CommonAssets getCommon() {
        return commonAssets;
    }
}
