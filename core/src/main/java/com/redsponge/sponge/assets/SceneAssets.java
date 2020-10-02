package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.animation.SAnimationGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SceneAssets implements Disposable {

    private AssetManager assetManager;

    private String sceneName;
    private HashMap<String, String> nameMap;
    private HashMap<String, SAnimationGroup> animationMap;
    private HashMap<String, AnimationNodeSystem> animationNodeSystemMap;

    private AssetMap assetMap;

    SceneAssets(AssetManager assetManager, String sceneName, AssetMap assetMap) {
        this.assetManager = assetManager;
        this.sceneName = sceneName;
        this.assetMap = assetMap;
        this.nameMap = new HashMap<>();
        this.animationMap = new HashMap<>();
        this.animationNodeSystemMap = new HashMap<>();
    }

    public void load() {
        FileHandle mainFolder = Gdx.files.internal(sceneName);
        loadTextures(mainFolder);
        loadAnimations(mainFolder);

        assetManager.finishLoading();

        for (SAnimationGroup value : animationMap.values()) {
            value.parseAnimations();
        }
    }

    private void loadAnimations(FileHandle mainFolder) {
        if(mainFolder.child("animation").exists()) {
            FileHandle animationDir = mainFolder.child("animation");
            FileHandle[] animationFiles = assetMap.list(animationDir);
            Set<String> animationNames = new HashSet<>();
            Set<FileHandle> animationNodeNames = new HashSet<>();
            for (FileHandle animationFile : animationFiles) {
                if(animationFile.extension().equals("sanim")) {
                    SAnimationGroup animations = new SAnimationGroup(assetManager, animationFile, Gdx.files.internal(sceneName + "/texture"));
                    animations.load();
                    animationMap.put(animationFile.nameWithoutExtension(), animations);
                    animationNames.add(animationFile.nameWithoutExtension());
                } else if(animationFile.extension().equals("animnodes")) {
                    animationNodeNames.add(animationFile);
                }
            }

            for (FileHandle animationNodeName : animationNodeNames) {
                if(animationNames.contains(animationNodeName.nameWithoutExtension())) {
                    AnimationNodeSystem system = new AnimationNodeSystem(animationNodeName, animationMap.get(animationNodeName.nameWithoutExtension()));
                    animationNodeSystemMap.put(animationNodeName.nameWithoutExtension(), system);
                } else {
                    System.out.println("Skipping node file " + animationNodeName + " since no matching animation file exists!");
                }
            }
        }
    }

    private void loadTextures(FileHandle mainFolder) {
        if(mainFolder.child("texture").exists()) {
            FileHandle textureFileDir = mainFolder.child("texture");
            FileHandle[] textureFiles = assetMap.list(textureFileDir);
            // TODO: more
            for (FileHandle textureFile : textureFiles) {
                System.out.println(textureFile);
                if(textureFile.extension().equals("atlas")) {
                    assetManager.load(textureFile.path(), TextureAtlas.class);
                } else {
                    assetManager.load(textureFile.path(), Texture.class);
                }
                nameMap.put(textureFile.name(), textureFile.path());
            }
        }
    }

    public void unload() {
        FileHandle mainFolder = Gdx.files.internal(sceneName);
        if(mainFolder.child("texture").exists()) {
            FileHandle textureFileDir = mainFolder.child("texture");
            FileHandle[] textureFiles = assetMap.list(textureFileDir);
            // TODO: more
            for (FileHandle textureFile : textureFiles) {
                System.out.println(textureFile);
                assetManager.unload(textureFile.name());
            }
        }

        for (SAnimationGroup value : animationMap.values()) {
            value.unload();
        }
    }

    public SAnimationGroup getAnimationGroup(String animationGroup) {
        return animationMap.get(animationGroup);
    }

    public AnimationNodeSystem getAnimationNodeSystemInstance(String name) {
        return animationNodeSystemMap.get(name).copy();
    }

    @Override
    public void dispose() {
        unload();
    }

    public <T> T get(String s) {
        return assetManager.get(nameMap.get(s));
    }
}
