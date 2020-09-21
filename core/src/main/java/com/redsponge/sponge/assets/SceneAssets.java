package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.animation.SAnimationGroup;
import com.redsponge.sponge.particles.Particle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SceneAssets implements Disposable {

    private AssetManager assetManager;

    private String sceneName;
    private HashMap<String, String> nameMap;
    private HashMap<String, SAnimationGroup> animationMap;
    private HashMap<String, AnimationNodeSystem> animationNodeSystemMap;

    private HashMap<String, Particle> particleEffectMap;

    SceneAssets(AssetManager assetManager, String sceneName) {
        this.assetManager = assetManager;
        this.sceneName = sceneName;
        this.nameMap = new HashMap<>();
        this.animationMap = new HashMap<>();
        this.animationNodeSystemMap = new HashMap<>();
        this.particleEffectMap = new HashMap<>();
    }

    public void load() {
        FileHandle mainFolder = Gdx.files.internal(sceneName);
        loadTextures(mainFolder);
        loadAnimations(mainFolder);
        loadParticles(mainFolder);

        assetManager.finishLoading();

        for (SAnimationGroup value : animationMap.values()) {
            value.parseAnimations(assetManager);
        }
        if(mainFolder.child("particle").child("textures.atlas").exists()) {
            TextureAtlas particleAtlas = assetManager.get(mainFolder.child("particle").child("textures.atlas").path());
            for (Particle value : particleEffectMap.values()) {
                value.load(particleAtlas);
            }
        }
    }

    private void loadParticles(FileHandle mainFolder) {
        if(mainFolder.child("particle").exists()) {
            if(!mainFolder.child("particle").child("textures.atlas").exists()) {
                Gdx.app.log("SceneAssets", "Skipping loading particles - missing textures.atlas");
                return;
            } else {
                assetManager.load(mainFolder.child("particle").child("textures.atlas").path(), TextureAtlas.class);
            }

            FileHandle[] particleFiles = mainFolder.child("particle").list();
            for (FileHandle particleFile : particleFiles) {
                if (particleFile.extension().equals("p")) {
                    Particle particle = new Particle(particleFile);
                    particleEffectMap.put(particleFile.nameWithoutExtension(), particle);
                }
            }
        }
    }

    private void loadAnimations(FileHandle mainFolder) {
        if(mainFolder.child("animation").exists()) {
            FileHandle animationDir = mainFolder.child("animation");
            FileHandle[] animationFiles = animationDir.list();
            Set<String> animationNames = new HashSet<>();
            Set<FileHandle> animationNodeNames = new HashSet<>();
            for (FileHandle animationFile : animationFiles) {
                if(animationFile.extension().equals("sanim")) {
                    SAnimationGroup animations = new SAnimationGroup(animationFile, Gdx.files.internal(sceneName + "/texture"));
                    animations.load(assetManager);
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
            FileHandle[] textureFiles = textureFileDir.list();
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
            FileHandle[] textureFiles = textureFileDir.list();
            // TODO: more
            for (FileHandle textureFile : textureFiles) {
                System.out.println(textureFile);
                assetManager.unload(textureFile.name());
            }
        }

        for (SAnimationGroup value : animationMap.values()) {
            value.unload(assetManager);
        }
    }

    public SAnimationGroup getAnimationGroup(String animationGroup) {
        Gdx.app.log("SA", animationMap.toString());
        return animationMap.get(animationGroup);
    }

    public AnimationNodeSystem getAnimationNodeSystemInstance(String name) {
        return animationNodeSystemMap.get(name).copy();
    }

    public Particle getParticle(String name) {
        return particleEffectMap.get(name);
    }

    @Override
    public void dispose() {
        unload();
    }

    public <T> T get(String s) {
        return assetManager.get(nameMap.get(s));
    }

    public Map<String, Particle> getParticles() {
        return particleEffectMap;
    }
}
