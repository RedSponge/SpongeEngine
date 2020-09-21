package com.redsponge.sponge.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationStreamComponent extends AnimationComponent {

    private Animation<TextureRegion> nextAnimation;
    private Runnable onAnimationSwitch;
    private boolean resetNextAnimationTime;
    private boolean doSkip;
    private boolean noNextAnimation;

    public AnimationStreamComponent(boolean active, boolean visible, Animation<TextureRegion> animation) {
        super(active, visible, animation);
    }

    @Override
    public void update(float delta) {
        if(nextAnimation != null || noNextAnimation) {
            if(isCompleted() || doSkip) {
                System.out.println("Switch animation!");
                if(nextAnimation != null) {
                    setAnimation(nextAnimation, resetNextAnimationTime);
                    nextAnimation = null;
                } else if(noNextAnimation) {
                    setVisible(false);
                    setActive(false);
                    noNextAnimation = false;
                } else {
                    throw new RuntimeException("Tried entering next animation when it's null and noNextAnimation is not set - this should never happen!");
                }
                if (onAnimationSwitch != null) {
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

    public void setNoNextAnimation(boolean noNextAnimation) {
        this.noNextAnimation = noNextAnimation;
    }
}
