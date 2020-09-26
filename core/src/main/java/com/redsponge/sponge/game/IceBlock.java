package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.ray3k.tenpatch.TenPatchDrawable;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.physics.TriggerHandler;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class IceBlock extends PSolid {

    public static final int[][][] frameOffsets = {
            {{1, 14}, {1, 9}},
            {{1, 14}, {1, 2}},
            {{1, 14}, {6, 6}}
    };

    private TenPatchDrawable blockPatch;
    private float meltTime;
    private boolean isMelting;

    private int maxHeight;
    private boolean isHorizontal;

    private static final float maxMeltTime = .5f;
    private float stayMeltTime;
    private float renderHeight;
    private boolean lastActivelyMelting;

    public IceBlock(Vector2 pos, Hitbox hitbox, boolean isHorizontal) {
        super(pos, hitbox);
        this.maxHeight = isHorizontal ? hitbox.getWidth() : hitbox.getHeight();
        this.isHorizontal = isHorizontal;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);

        blockPatch = new TenPatchDrawable(new int[] {
            1, 14
        }, new int[] {1, 9}, true, new TextureRegion(scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("melting_block", 1)));
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
        renderHeight = (int) Math.max(i.apply(1 - meltTime / maxMeltTime) * maxHeight, 2);
        if(!isHorizontal) {
            setHeight((int) renderHeight);
            if (isActivelyMelting()) {
                if (((GameScene) getScene()).getPlayer().isRiding(this)) {
                    setCollidable(false);
                    ((GameScene) getScene()).getPlayer().moveToY(getY() + getHeight()+ 5);
                    setCollidable(true);
                    ((FirePlayer) ((GameScene) getScene()).getPlayer()).getJumpGraceTime().setValue(0.5f);
                }
            } else if (lastActivelyMelting && !isActivelyMelting()) {
                if (((GameScene) getScene()).getPlayer().isRiding(this)) {
                    setCollidable(false);
                    ((GameScene) getScene()).getPlayer().moveToY(getY() + getHeight() + 5);
                    setCollidable(true);
//                ((FirePlayer)((GameScene) getScene()).getPlayer()).getVelocity().y = 120;
                }
            }
            lastActivelyMelting = isActivelyMelting();
//            setCollidable(!isMelting);
        } else {
            setWidth((int) renderHeight);
            if(check(((GameScene)getScene()).getPlayer())) {
                setCollidable(false);
                ((GameScene) getScene()).getPlayer().moveToX(getRight() + 5);
                setCollidable(true);
            }
            lastActivelyMelting = isActivelyMelting();
//            setCollidable(!isMelting);
        }
    }

    @Override
    public void render() {
        super.render();
        blockPatch.getColor().a = Math.max(renderHeight / maxHeight, 0.4f);
        blockPatch.draw(SpongeGame.i().getBatch(), getX(), getY(), getWidth(), getHeight());
        SpongeGame.i().getShapeDrawer().rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void onTrigger(TriggerHandler t) {

    }

    public boolean isActivelyMelting() {
        return meltTime > 0 && meltTime < maxMeltTime;
    }

    public void reMelt() {
        stayMeltTime = 1.5f;
    }

    public boolean isReforming() {
        return meltTime > 0 && stayMeltTime <= 0;
    }
}
