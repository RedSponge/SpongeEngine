package com.redsponge.sponge.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.SpongeGame;

import java.util.HashSet;
import java.util.Set;

public class Particle implements Disposable {

    private ParticleEffect effect;
    private ParticleEffectPool pool;

    private Set<PooledEffect> effects;
    private Set<PooledEffect> toRemove;

    private FileHandle file;

    public Particle(FileHandle file) {
        this.file = file;
        effects = new HashSet<>();
        toRemove = new HashSet<>();
    }

    public void load(TextureAtlas atlas) {
        if(effect != null) {
            effect.dispose();
            effect = null;
        }
        effect = new ParticleEffect();
        effect.load(file, atlas);
        pool = new ParticleEffectPool(effect, 16, 64);
    }

    public PooledEffect spawnEffect(float x, float y) {
        PooledEffect pooledEffect = pool.obtain();
        pooledEffect.setPosition(x, y);
        effects.add(pooledEffect);
        return pooledEffect;
    }

    public void removeEffect(PooledEffect effect) {
        toRemove.add(effect);
    }

    public void update(float delta) {
        for (PooledEffect pooledEffect : effects) {
            pooledEffect.update(delta);
            if(pooledEffect.isComplete()) {
                toRemove.add(pooledEffect);
            }
        }

        for (PooledEffect pooledEffect : toRemove) {
            pooledEffect.free();
        }
        effects.removeAll(toRemove);
        toRemove.clear();
    }

    public void render() {
        for (PooledEffect pooledEffect : effects) {
            pooledEffect.draw(SpongeGame.i().getBatch());
        }
    }

    @Override
    public void dispose() {
        if(effect != null) {
            effect.dispose();
            effect = null;
        }
    }

    public ParticleEffect getParticleEffect() {
        return effect;
    }

    public void setParticleEffect(ParticleEffect particleEffect) {
        this.effect = particleEffect;
    }
}
