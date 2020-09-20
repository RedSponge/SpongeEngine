package com.redsponge.sponge.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class SAnimationGroup {

    private Map<String, SAnimation> containedAnimations;
    private TextureAtlas atlas;
    private FileHandle resourceDir;
    private AssetManager assetManager;
    private String animText;
    private boolean loaded;
    private String atlasName;

    public SAnimationGroup(AssetManager assetManager, FileHandle animFile, FileHandle resourceDir) {
        this.assetManager = assetManager;
        this.resourceDir = resourceDir;
        this.animText = animFile.readString();

        JsonReader reader = new JsonReader();
        JsonValue value = reader.parse(animText);

        this.atlasName = value.getString("atlas");
    }

    public void load() {
        if(loaded) return;
        loaded = true;

        assetManager.load(resourceDir.child(atlasName).path(), TextureAtlas.class);
    }

    public void parseAnimations() {
        if(!loaded) {
            throw new RuntimeException("Trying to parse non-loaded animation!");
        }

        JsonReader reader = new JsonReader();
        JsonValue value = reader.parse(animText);

        atlas = assetManager.get(resourceDir.child(atlasName).path());

        containedAnimations = new HashMap<>();
        JsonValue animations = value.get("animations");
        for (JsonValue animation : animations) {
            PlayMode playMode = PlayMode.valueOf(animation.getString("play_mode"));

            Array<SFrame<TextureRegion>> frames = new Array<>();
            for(JsonValue frame : animation.get("frames")) {
                String name = frame.getString("frame");
                int index = frame.getInt("idx");
                int duration = frame.getInt("length");
                frames.add(new SFrame<>(atlas.findRegion(name, index), duration));
            }
            containedAnimations.put(animation.name, new SAnimation(frames, playMode));
        }
    }

    public SAnimation get(String idle) {
        return containedAnimations.get(idle);
    }

    public void unload() {
        assetManager.unload(resourceDir.child(atlasName).path());
    }
}
