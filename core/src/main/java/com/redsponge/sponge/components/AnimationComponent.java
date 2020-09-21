package com.redsponge.sponge.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent extends DrawnComponent {

    protected float time;
    protected Animation<TextureRegion> animation;
    protected float speed = 1;

    public AnimationComponent(boolean active, boolean visible, Animation<TextureRegion> animation) {
        super(active, visible, animation.getKeyFrame(0));
        this.animation = animation;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.time += delta * speed;
        time = Math.max(time, 0);
        this.setRendered(animation.getKeyFrame(time));
    }

    public AnimationComponent setAnimation(Animation<TextureRegion> animation) {
        if(animation != this.animation) {
            time = 0;
            this.animation = animation;
        }
        return this;
    }

    public AnimationComponent setAnimation(Animation<TextureRegion> animation, boolean resetTime) {
        if(resetTime) {
            resetTime();
        }
        return setAnimation(animation);
    }

    public void resetTime() {
        time = 0;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public boolean isCompleted() {
        if(speed > 0) {
            return animation.isAnimationFinished(time);
        } else {
            return time <= 0;
        }
    }

    public void setAnimationSpeed(float speed) {
        this.speed = speed;
    }
}
