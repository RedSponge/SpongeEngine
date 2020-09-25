package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class Button extends PSolid implements Connection {


    public enum Orientation {
        LEFT(new Hitbox(0, 2, 3, 12), 270, new Vector2(-1, 0)),
        UP(new Hitbox(2, 0, 12, 3), 0, new Vector2(0, -1)),
        RIGHT(new Hitbox(13, 2, 3, 12), 90, new Vector2(1, 0)),
        DOWN(new Hitbox(2, 13, 12, 3), 180, new Vector2(0, 1))

        ;

        private final float rotation;
        private final Vector2 hitDirection;
        private final Hitbox hitbox;

        Orientation(Hitbox hitbox, float rotation, Vector2 hitDirection) {
            this.hitbox = hitbox;
            this.rotation = rotation;
            this.hitDirection = hitDirection;
        }

        public Hitbox getHitbox() {
            return new Hitbox().set(hitbox);
        }

        public float getRotation() {
            return rotation;
        }

        public Vector2 getHitDirection() {
            return hitDirection;
        }
    }

    private DrawnComponent drawn;
    private boolean on;
    private Orientation orientation;
    private TextureRegion offRegion, onRegion;
    private String connection;

    public Button(Vector2 pos, Orientation orientation, String connection) {
        super(pos, orientation.getHitbox());
        this.orientation = orientation;
        this.connection = connection;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        Connections.registerConnection(connection, this, true);
        offRegion = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("button_off");
        onRegion = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("button_on");
        drawn = new DrawnComponent(true, true, offRegion);
        drawn.setRotation(orientation.getRotation());
        drawn.setOriginX(8);
        drawn.setOriginY(8);
        drawn.setOffsetY(-getHitbox().getBottom());
        drawn.setOffsetX(-getHitbox().getLeft());
        add(drawn);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(Controls.DEBUG.isJustPressed()) {
            on = !on;
        }
        drawn.setRendered(on ? onRegion : offRegion);
    }

    @Override
    public void render() {
//        drawn.render();
        super.render();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Button setOn(boolean on) {
        this.on = on;
        return this;
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void removed() {
        super.removed();
        Connections.unregisterConnection(connection, this);
    }
}
