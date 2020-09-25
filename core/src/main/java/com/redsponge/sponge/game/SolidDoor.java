package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.ray3k.tenpatch.TenPatchDrawable;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;
import com.redsponge.sponge.util.UMath;

public class SolidDoor extends Entity {

    private boolean isVertical;
    private PSolid a, b;
    private boolean shouldAddSolids;
    private boolean isToggled;
    private float doorTime;
    private static final float maxDoorTime = 0.3f;
    private String connection;

    private TenPatchDrawable doorDrawable;

    public SolidDoor(Vector2 pos, int width, int height, boolean isVertical, String connection) {
        super(pos);
        this.connection = connection;
        getHitbox().set(0, 0, width, height);
        this.isVertical = isVertical;

        initSolids();
    }


    private void initSolids() {
        int origin = isVertical ? getY() : getX();
        int sideSize = isVertical ? getHeight() : getWidth();

        int center = origin + sideSize / 2;
        int partSize = sideSize / 2;

        Vector2 template = isVertical ? Vector2.Y : Vector2.X;
        int hbWidth = isVertical ? getWidth() : getWidth() / 2;
        int hbHeight = isVertical ? getHeight() / 2 : getHeight();
        a = new PSolid(new Vector2(getX(), getY()), new Hitbox(0, 0, hbWidth, hbHeight));
        b = new PSolid(new Vector2(getX(), getY()).add(template.cpy().scl(partSize)), new Hitbox(0, 0, hbWidth, hbHeight));
    }

    @Override
    public void update(float delta) {
        if(shouldAddSolids) {
            getScene().add(a);
            getScene().add(b);
            shouldAddSolids = false;
            return;
        }
        if(Controls.DEBUG.isJustPressed()) {
            isToggled = !isToggled;
        }
        isToggled = Connections.isActive(connection);
        updateDoorParts(delta);
        super.update(delta);
    }

    private void updateDoorParts(float delta) {
        doorTime += delta * (isToggled ? 1 : -1);
        doorTime = UMath.clamp(doorTime, 0, maxDoorTime);
        float progress = Interpolation.exp5.apply(0, (isVertical ? getHeight() : getWidth()) / 2f, doorTime / maxDoorTime);
        if(isVertical) {
            a.moveToY(getY() - progress);
            b.moveToY(getY() + getHeight() / 2f + progress);
        } else {
            a.moveToX(getX() - progress);
            b.moveToX(getX() + getWidth() / 2f + progress);
        }
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        shouldAddSolids = true;
        doorDrawable = new TenPatchDrawable(new int[] {3, 13}, new int[] {3, 13}, true, scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("door_fire"));
    }

    @Override
    public void removeSelf() {
        a.removeSelf();
        b.removeSelf();
        super.removeSelf();
    }

    @Override
    public void render() {
        doorDrawable.draw(SpongeGame.i().getBatch(), a.getX(), a.getY(), a.getWidth(), a.getHeight());
        doorDrawable.draw(SpongeGame.i().getBatch(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }
}
