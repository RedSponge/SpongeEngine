package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.animation.SAnimationGroup;
import com.redsponge.sponge.util.Logger;

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
    private Array<FileHandle> loadedFiles;

    SceneAssets(AssetManager assetManager, String sceneName, AssetMap assetMap) {
        this.assetManager = assetManager;
        this.sceneName = sceneName;
        this.assetMap = assetMap;
        this.nameMap = new HashMap<>();
        this.animationMap = new HashMap<>();
        this.animationNodeSystemMap = new HashMap<>();
        this.loadedFiles = new Array<>();
    }

    public void load() {
        FileHandle mainFolder = Gdx.files.internal(sceneName);

        Logger.debug(this, "Loading textures for", sceneName);
        loadTextures(mainFolder);

        Logger.debug(this, "Loading animations for", sceneName);
        loadAnimations(mainFolder);

        Logger.debug(this, "Loading sounds for", sceneName);
        loadSounds(mainFolder);

        Logger.debug(this, "Calling AssetManager.finishLoading for", sceneName);
        assetManager.finishLoading();

        Logger.debug(this, "Parsing animations for", sceneName);
        for (SAnimationGroup value : animationMap.values()) {
            value.parseAnimations();
        }
    }

    private void loadSounds(FileHandle mainFolder) {
        if(mainFolder.child("sound").exists()) {
            FileHandle soundDir = mainFolder.child("sound");
            FileHandle[] soundFiles = assetMap.list(soundDir);
            for (FileHandle soundFile : soundFiles) {
                Logger.debug(this, "Loading sound", soundFile);
                assetManager.load(soundFile.path(), Sound.class);
            }
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
                    Logger.debug(this, "Loading SAnimation", animationFile);
                    SAnimationGroup animations = new SAnimationGroup(assetManager, animationFile, Gdx.files.internal(sceneName + "/texture"));
                    animations.load();
                    animationMap.put(animationFile.nameWithoutExtension(), animations);
                    animationNames.add(animationFile.nameWithoutExtension());
                } else if(animationFile.extension().equals("animnodes")) {
                    Logger.debug(this, "Found Animation-Nodes File", animationFile);
                    animationNodeNames.add(animationFile);
                }
            }

            Logger.debug(this, "Loading Animation Nodes");
            for (FileHandle animationNodeName : animationNodeNames) {
                if(animationNames.contains(animationNodeName.nameWithoutExtension())) {
                    Logger.debug(this, "Loading Animation Node", animationNodeName, "for", sceneName);

                    AnimationNodeSystem system = new AnimationNodeSystem(animationNodeName, animationMap.get(animationNodeName.nameWithoutExtension()));
                    animationNodeSystemMap.put(animationNodeName.nameWithoutExtension(), system);
                } else {
                    Logger.warn(this, "Skipping node file " + animationNodeName + " since no matching animation file exists! (for", sceneName + ")");
                }
            }
        }
    }

    private void loadTextures(FileHandle mainFolder) {
        if(mainFolder.child("texture").exists()) {
            FileHandle textureFileDir = mainFolder.child("texture");
            FileHandle[] textureFiles = assetMap.list(textureFileDir);
            for (FileHandle textureFile : textureFiles) {
                if(textureFile.extension().equals("atlas")) {
                    Logger.debug(this, "Loading Texture Atlas", textureFile, "for", sceneName);
                    assetManager.load(textureFile.path(), TextureAtlas.class);
                } else {
                    Logger.debug(this, "Loading Texture", textureFile, "for", sceneName);
                    assetManager.load(textureFile.path(), Texture.class);
                }
                nameMap.put(textureFile.name(), textureFile.path());
            }
        }
    }

    public void unload() {
        FileHandle mainFolder = Gdx.files.internal(sceneName);
        Logger.debug(this, "Unloading Scene", sceneName);
        
        Logger.debug(this, "Unload Textures for", sceneName);
        unloadTextures(mainFolder);

        Logger.debug(this, "Unloading SAnimations for", sceneName);
        for (SAnimationGroup value : animationMap.values()) {
            value.unload();
        }
    }

    private void unloadTextures(FileHandle mainFolder) {
        if(mainFolder.child("texture").exists()) {
            FileHandle textureFileDir = mainFolder.child("texture");
            FileHandle[] textureFiles = assetMap.list(textureFileDir);
            // TODO: more
            for (FileHandle textureFile : textureFiles) {
                Logger.debug(this, "Unloading Texture", textureFile);
                assetManager.unload(nameMap.get(textureFile.name()));
            }
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
