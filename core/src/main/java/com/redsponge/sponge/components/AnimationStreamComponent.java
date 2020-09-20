package com.redsponge.sponge.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationStreamComponent extends AnimationComponent {

    private Animation<TextureRegion> nextAnimation;
    private Runnable onAnimationSwitch;
    private boolean resetNextAnimationTime;
    private boolean doSkip;

    public AnimationStreamComponent(boolean active, boolean visible, Animation<TextureRegion> animation) {
        super(active, visible, animation);
    }

    @Override
    public void update(float delta) {
        if(nextAnimation != null) {
            if(animation.isAnimationFinished(time) || doSkip) {
                setAnimation(nextAnimation, resetNextAnimationTime);
                nextAnimation = null;
                if(onAnimationSwitch != null) {
                    onAnimationSwitch.run();
                }
            }
        }
        super.update(delta);
    }

    public AnimationStreamComponent setNextAnimation(Animation<TextureRegion> nextAnimation) {
        this.nextAnimation = nextAnimation;
        return this;
    }

    public Animation<TextureRegion> getNextAnimation() {
        return nextAnimation;
    }

    public Runnable getOnAnimationSwitch() {
        return onAnimationSwitch;
    }

    public AnimationStreamComponent setOnAnimationSwitch(Runnable onAnimationSwitch) {
        this.onAnimationSwitch = onAnimationSwitch;
        return this;
    }

    public boolean isResetNextAnimationTime() {
        return resetNextAnimationTime;
    }

    public AnimationStreamComponent setResetNextAnimationTime(boolean resetNextAnimationTime) {
        this.resetNextAnimationTime = resetNextAnimationTime;
        return this;
    }

    public boolean isDoSkip() {
        return doSkip;
    }

    public AnimationStreamComponent setDoSkip(boolean doSkip) {
        this.doSkip = doSkip;
        return this;
    }
}
