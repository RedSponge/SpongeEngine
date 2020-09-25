package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.particles.Particle;
import com.redsponge.sponge.physics.PTrigger;
import com.redsponge.sponge.screen.Scene;

public class SteamColumn extends PTrigger {

    private PooledEffect particle;

    public SteamColumn(Vector2 pos, int width, int height) {
        super(pos);
        getHitbox().set(0, 0, width, height);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        particle = scene.getAssets().getParticle("smoke").spawnEffect(getX(), getY());
        particle.getEmitters().get(0).getSpawnWidth().setHigh(getWidth());
        particle.getEmitters().get(0).getLife().setHigh((getHeight() + 30) / 50f * 1000);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void removed() {
        getScene().getAssets().getParticle("smoke").removeEffect(particle);
        super.removed();
    }
}
