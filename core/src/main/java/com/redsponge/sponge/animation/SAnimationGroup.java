package com.redsponge.sponge.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
            final Vector2[] offsets = new Vector2[] {new Vector2(), new Vector2()};

            if(animation.has("offsets")) {
                offsets[0] = parseVector2(animation.get("offsets").get(0));
                offsets[1] = parseVector2(animation.get("offsets").get(1));
            }

            Array<SFrame<TextureRegion>> frames = new Array<>();
            for(JsonValue frame : animation.get("frames")) {
                String name = frame.getString("frame");
                int index = frame.getInt("idx");
                int duration = frame.getInt("length");
                frames.add(new SFrame<>(atlas.findRegion(name, index), duration));
            }
            containedAnimations.put(animation.name, new SAnimation(frames, playMode, offsets));
        }
    }

    private Vector2 parseVector2(JsonValue value) {
        return new Vector2(value.get(0).asFloat(), value.get(1).asFloat());
    }

    public SAnimation get(String idle) {
        return containedAnimations.get(idle);
    }

    public void unload() {
        assetManager.unload(resourceDir.child(atlasName).path());
    }
}
