package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.UMath;

import java.util.ArrayList;

public class CameraManager extends Entity {

    private MapManager mm;
    private int yLevel;
    private float transformationTime;
    private PActor player;
    private int maxLevel;

    private static final int chopSize = 270;
    private float yFrom;
    private int yTo;

    private static final float maxTime = 0.5f;

    public CameraManager() {
        super(new Vector2());
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        try {
            mm = getScene().first(MapManager.class);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Couldn't find mm yet");
        }
    }

    public void setMM(MapManager mm) {
        this.mm = mm;
    }

    @Override
    public boolean check(Entity other) {
        return true;
    }

    private int getPlayerY() {
        return (int) ((GameScene)getScene()).getPlayer().getPosition().y;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(mm == null) {
            try {
                mm = all(new ArrayList<>(), MapManager.class).get(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Couldn't find mm yet");
                return;
            }
        }
        maxLevel = mm.getMapHeight() / chopSize;
        int current = getPlayerY() / chopSize;
        if(current != yLevel) {
//            yFrom = yLevel * chopSize;
            yTo = current * chopSize;
            yFrom = getScene().viewport.getCamera().position.y;
            yLevel = current;
            transformationTime = 0;
        }
        transformationTime += delta;
        float maxTime = 0.5f;

        getScene().viewport.getCamera().position.y = Interpolation.exp5.apply(yFrom, yTo + 135, Math.min(transformationTime / maxTime, 1));
    }

    public boolean isTransitioning() {
        return transformationTime / maxTime < 1;
    }
}
