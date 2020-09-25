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

    private float minFireballY;
    private float maxFireballY;

    private float time;

    public FireballLine(Vector2 pos, Vector2 size, int density, Vector2 direction, float speed) {
        super(pos);
        this.speed = speed;
        getHitbox().set(0, 0, (int) size.x, (int) size.y);
        this.density = density;
        this.direction = direction;
        fireballs = new LinkedList<>();
        this.minFireballX = getX() - 10;
        this.maxFireballX = getRight() + 4;
        this.minFireballY = getY() - 10;
        this.maxFireballY = getTop() + 10;
        System.out.println(minFireballX + " " + maxFireballX + " " + minFireballY + " " + maxFireballY);
    }

    private void spawnFireballs() {
        float spacingX = (maxFireballX - minFireballX) / density * Math.abs(direction.x);
        float spacingY = (maxFireballY - minFireballY) / density * Math.abs(direction.y);
//        fireballs.add(new Fireball(new Vector2(minFireballX, getY())));
        for (int i = 0; i < ((int) density); i++) {
            fireballs.add(new Fireball(new Vector2(minFireballX + i * spacingX, minFireballY + i * spacingY)));
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
        float vx = speed * delta * direction.x;
        float vy = speed * delta * direction.y;
        for (Fireball fireball : fireballs) {
            fireball.moveX(vx);
            fireball.moveY(vy);
            if(fireball.getX() > maxFireballX) {
                fireball.setX(minFireballX);
            }
            if(fireball.getX() < minFireballX) {
                fireball.setX(maxFireballX);
            }
            if(fireball.getY() > maxFireballY) {
                fireball.setY(minFireballY);
            }
            if(fireball.getY() < minFireballY) {
                fireball.setY(maxFireballY);
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
