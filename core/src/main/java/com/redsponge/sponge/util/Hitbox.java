package com.redsponge.sponge.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import text.formic.Stringf;

public class Hitbox {

    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle rect;

    public Hitbox() {
        this(0, 0, 0, 0);
    }

    public Hitbox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rectangle();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeft(int left) {
        x = left;
    }

    public int getLeft() {
        return x;
    }

    public void setRight(int right) {
        x = right - width;
    }

    public int getRight() {
        return x + width;
    }

    public void setBottom(int bottom) {
        this.y = bottom;
    }

    public int getBottom() {
        return this.y;
    }

    public void setTop(int top) {
        this.y = top - height;
    }

    public int getTop() {
        return y + height;
    }

    public Vector2 getOrigin() {
        return new Vector2(x, y);
    }

    public Hitbox mirrorX(int axis) {
        x = axis - (x - axis) - width;
        return this;
    }

    public Hitbox mirrorY(int axis) {
        y = axis - (y - axis) - height;
        return this;
    }

    public Hitbox inflate(int amount) {
        return new Hitbox(x - amount, y - amount, width + amount * 2, height + amount * 2);
    }

    public boolean intersects(Hitbox other) {
        return getRight() > other.getLeft() && other.getRight() > getLeft() && getTop() > other.getBottom() && other.getTop() > getBottom();
    }

    public boolean contains(Vector2 point) {
        return point.x >= x && point.x < x + width && point.y >= y && point.y < y + height;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Hitbox) {
            Hitbox r = (Hitbox) obj;
            return r.x == x && r.y == y && r.width == width && r.height == height;
        }
        return false;
    }

    public Hitbox add(Vector2 p) {
        return new Hitbox(x + (int) p.x, y + (int) p.y, width, height);
    }

    public Hitbox sub(Vector2 p) {
        return new Hitbox(x - (int) p.x, y - (int) p.y, width, height);
    }

    public Hitbox mul(Vector2 p) {
        return new Hitbox(x * (int) p.x, y * (int) p.y, width * (int) p.x, height * (int) p.y);
    }
    public Hitbox mul(int n) {
        return new Hitbox(x * n, y * n, width * n, height * n);
    }

    public Hitbox div(Vector2 p) {
        return new Hitbox(x / (int) p.x, y / (int) p.y, width / (int) p.x, height / (int) p.y);
    }
    public Hitbox div(int n) {
        return new Hitbox(x / n, y / n, width / n, height / n);
    }



    @Override
    public String toString() {
        return Stringf.format("[%d, %d, %d, %d]", x, y, width, height);
    }

    public void set(Hitbox other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    public void set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getRectangle() {
        return rect.set(x, y, width, height);
    }
}
