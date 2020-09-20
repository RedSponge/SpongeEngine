package com.redsponge.sponge.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.redsponge.sponge.animation.Values.ValueHolder;
import com.redsponge.sponge.animation.Values.VariableValueHolder;

import java.util.HashMap;

public class AnimationNodeSystem implements Values.VariableSupplier {

    private String active;
    private SAnimationGroup animations;

    private HashMap<String, Integer> suppliedInts;
    private HashMap<String, Float> suppliedFloats;
    private HashMap<String, Boolean> suppliedBools;

    private HashMap<String, AnimationNode> animationNodes;
    private HashMap<String, AnimationNode> prefabs;

    private FileHandle connectedFile;

    public AnimationNodeSystem(String initialAnimation, SAnimationGroup animations) {
        this.active = initialAnimation;
        this.animations = animations;
        suppliedFloats = new HashMap<>();
        suppliedInts = new HashMap<>();
        suppliedBools = new HashMap<>();
        this.animationNodes = new HashMap<>();
        this.prefabs = new HashMap<>();
    }

    public AnimationNodeSystem(FileHandle jsonFile, SAnimationGroup animations) {
        this(new JsonReader().parse(jsonFile.read()).getString("initial"), animations);
        this.connectedFile = jsonFile;
        addNodes(jsonFile);
    }

    public void putInt(String name, int value) {
        suppliedInts.put(name, value);
    }

    public void putFloat(String name, float value) {
        suppliedFloats.put(name, value);
    }

    public void putBoolean(String name, boolean value) {
        suppliedBools.put(name, value);
    }

//    public <T> void putParam(String name, T value) {
//        suppliedValues.put(name, value);
//    }

    public void putNode(String name, AnimationNode node) {
        animationNodes.put(name, node);
    }

    public void update() {
        AnimationNode activeNode = animationNodes.get(active);
        String newActive = activeNode.testChanges();
        if(newActive != null) {
            active = newActive;
            update();
        }
    }

    public Animation<TextureRegion> getActiveAnimation() {
        return animations.get(active).getBuiltAnimation();
    }


    @Override
    public <T> T supply(String variableName, Class<T> clazz) {
        if (Integer.class.equals(clazz)) {
            return (T) suppliedInts.get(variableName);
        } else if (Float.class.equals(clazz)) {
            return (T) suppliedFloats.get(variableName);
        } else if (Boolean.class.equals(clazz)) {
            return (T) suppliedBools.get(variableName);
        }
        throw new RuntimeException("Cannot supply variable '"  + variableName + "'");
    }

    public <T> VariableValueHolder<T> getHolder(String name) {
        return new VariableValueHolder<>(this, name);
    }

    public void addNodes(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(file);
        if(json.hasChild("prefabs")) {
            for (JsonValue jsonValue : json.get("prefabs")) {
                prefabs.put(jsonValue.name(), new AnimationNode(jsonValue, this, prefabs));
            }
        }

        for (JsonValue node : json.get("nodes")) {
            putNode(node.name(), new AnimationNode(node, this, prefabs));
        }
    }

    public AnimationNodeSystem copy() {
        AnimationNodeSystem system = new AnimationNodeSystem(active, animations);
        system.addNodes(connectedFile);
//        for (String s : animationNodes.keySet()) {
//            system.animationNodes.put(s, animationNodes.get(s));
//        }
        return system;
    }

    public SAnimationGroup getAnimationGroup() {
        return animations;
    }
}
