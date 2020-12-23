package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.particles.Particle;
import com.redsponge.sponge.test.PresentationSettings;

public class ParticleRendererEntity extends Entity {

    public ParticleRendererEntity() {
        super(new Vector2(0, 0));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Particle value : getScene().getAssets().getParticleEffectMap().values()) {
            value.update(delta);
        }
    }

    @Override
    public void render() {
        super.render();
        if(PresentationSettings.doParticles) {
            for (Particle value : getScene().getAssets().getParticleEffectMap().values()) {
                value.render();
            }
        }
    }
}
