package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.components.TimedAction;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.test.Player;
import com.redsponge.sponge.test.PresentationSettings;
import com.redsponge.sponge.util.UMath;

public class CameraHandler extends Entity {

    private Entity target;
    private TimedAction shakeTimer;

    public CameraHandler(Entity target) {
        super(new Vector2(0, 0));
        this.target = target;
        add(shakeTimer = new TimedAction());
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        EventBus.getInstance().registerListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        EventBus.getInstance().removeListener(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setX(target.getX());
        setY(target.getY());

        getScene().getViewport().getCamera().position.x = UMath.clamp(getPosition().x, getScene().getWidth() / 2f, 10000);
        getScene().getViewport().getCamera().position.y = UMath.clamp(getPosition().y, getScene().getHeight() / 2f, 1000);
        if(shakeTimer.isActive()) {
            float dx = MathUtils.random(-5f, 5f);
            float dy = MathUtils.random(-5f, 5f);
            getScene().getViewport().getCamera().position.x += dx;
            getScene().getViewport().getCamera().position.y += dy;
        }
    }

    @EventHandler
    public void onShakeEvent(ShakeEvent shakeEvent) {
        if(!PresentationSettings.doShake) return;
        if(shakeEvent.override) shakeTimer.setValue(0);
        shakeTimer.setValue(shakeTimer.getValue() + shakeEvent.time);
    }
}
