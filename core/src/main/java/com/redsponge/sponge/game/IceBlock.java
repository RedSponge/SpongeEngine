package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.ray3k.tenpatch.TenPatchDrawable;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class IceBlock extends PSolid {

    public static final int[][][] frameOffsets = {
            {{1, 14}, {1, 9}},
            {{1, 14}, {1, 2}},
            {{1, 14}, {6, 6}}
    };

    private TenPatchDrawable blockPatch;
    private TenPatchDrawable melterPatch;
    private float meltTime;
    private boolean isMelting;
    private Animation<TextureRegion> melterAnimation;

    private int maxHeight;

    private static final float maxMeltTime = .5f;
    private float stayMeltTime;
    private float renderHeight;

    public IceBlock(Vector2 pos, Hitbox hitbox) {
        super(pos, hitbox);
        this.maxHeight = hitbox.getHeight();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        melterAnimation = scene.getAssets().getAnimationGroup("world").get("melter").getBuiltAnimation();

        blockPatch = new TenPatchDrawable(new int[] {
            1, 14
        }, new int[] {1, 9}, true, new TextureRegion(scene.getAssets().<Texture>get("iceblock.png")));
        melterPatch = new TenPatchDrawable(new int[] {
                1, 14,
        }, new int[] {}, true, null);
    }

    public void toggleMelt() {
        isMelting = !isMelting;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stayMeltTime -= delta;
        isMelting = stayMeltTime > 0;
        if(isMelting) {
            meltTime += delta;
            meltTime = Math.min(meltTime, maxMeltTime);
        } else {
            meltTime -= delta;
            meltTime = Math.max(meltTime, 0);
        }
        Interpolation i = isMelting ? Interpolation.exp5In : Interpolation.exp5Out;
        renderHeight = Math.max(i.apply(1 - meltTime / maxMeltTime) * maxHeight, 2);
        setHeight((int) renderHeight);
        if(isActivelyMelting()) {
            melterPatch.setRegion(melterAnimation.getKeyFrame(meltTime));
        }
    }

    @Override
    public void render() {
        super.render();
        blockPatch.draw(SpongeGame.i().getBatch(), getX(), getY(), getWidth(), renderHeight);
        if(isActivelyMelting()) {
            melterPatch.draw(SpongeGame.i().getBatch(), getX() - 2, getY() + renderHeight - 10, getWidth() + 4, 16);
        }
    }

    public boolean isActivelyMelting() {
        return meltTime > 0 && meltTime < maxMeltTime;
    }

    public void reMelt() {
        stayMeltTime = 3;
    }

    public boolean isReforming() {
        return meltTime > 0 && stayMeltTime <= 0;
    }
}
