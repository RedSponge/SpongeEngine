package com.redsponge.sponge.light;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class LightMaps implements Disposable {

    private final HashMap<LightBlending, LightMap> maps;
    private final FitViewport viewport;

    public LightMaps(FitViewport viewport) {
        this.viewport = viewport;
        maps = new HashMap<>();
    }

    public LightMap get(LightBlending blending) {
        if(!maps.containsKey(blending)) {
            LightMap map = new LightMap(viewport, blending.getDefaultColor(), blending);
            maps.put(blending, map);
            return map;
        }
        return maps.get(blending);
    }

    public void resize(int width, int height) {
        for (LightMap value : maps.values()) {
            value.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        for (LightMap value : maps.values()) {
            value.dispose();
        }
    }
}
