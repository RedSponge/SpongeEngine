package com.redsponge.sponge.game;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.entity.Entity;

import java.util.LinkedList;

public class FireballLine extends Entity {

    private LinkedList<Fireball> fireballs;
    private float density;
    private Vector2 direction;
    private float speed;

    private float minFireballX;
    private float maxFireballX;
    private float time;

    public FireballLine(Vector2 pos, Vector2 size, int density, Vector2 direction, float speed) {
        super(pos);
        this.speed = speed;
        getHitbox().set((int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
        this.density = density;
        this.direction = direction;
        fireballs = new LinkedList<>();
        this.minFireballX = getX() - 10;
        this.maxFireballX = getRight() + 4;
    }

    private void spawnFireballs() {
        float spacing = (maxFireballX - minFireballX) / density;
//        fireballs.add(new Fireball(new Vector2(minFireballX, getY())));
        for (int i = 0; i < ((int) density); i++) {
            fireballs.add(new Fireball(new Vector2(minFireballX - i * spacing, getY())));
        }
        for (Fireball fireball : fireballs) {
            getScene().add(fireball);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(time == 0) {
            spawnFireballs();
        }
        time += delta;
        for (Fireball fireball : fireballs) {
            fireball.moveX(speed * delta);
            if(fireball.getX() > maxFireballX) {
                fireball.setX(minFireballX);
            }
        }
    }

    @Override
    public void removeSelf() {
        for (Fireball fireball : fireballs) {
            fireball.removeSelf();
        }
        super.removeSelf();
    }
}
