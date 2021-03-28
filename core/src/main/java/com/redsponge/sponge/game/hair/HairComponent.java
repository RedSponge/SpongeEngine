package com.redsponge.sponge.game.hair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Component;

public class HairComponent extends Component {

    private final Array<HairPoint> hairPoints;
    private final Vector2 source;
    private final int length;

    private final Vector2 tmpVector;
    private boolean flipped;

    private float scale;

    public HairComponent(boolean active, boolean visible, Vector2 source, int length) {
        super(active, visible);
        this.hairPoints = new Array<>();
        this.source = source;
        this.length = length;

        hairPoints.add(new HairPoint(new Vector2(), 0, 8));
        for (int i = 1; i < length; i++) {
            hairPoints.add(new HairPoint(new Vector2(-2, 1),2, Math.max(10 - i, 3)));
        }

        tmpVector = new Vector2();
        this.scale = 1;
    }

    @Override
    public void begin() {

    }

    @Override
    public void update(float delta) {
        hairPoints.get(0).pos.set(source).scl(scale).add(getEntity().getPositionf());
        for (int i = hairPoints.size - 1; i > 0; i--) {
            hairPoints.get(i).pos.lerp(hairPoints.get(i - 1).getOffsettedPosition(flipped, scale), 0.1f);
        }

        for (int i = hairPoints.size - 1; i > 0; i--) {
            if(hairPoints.get(i).pos.dst2(hairPoints.get(i - 1).getOffsettedPosition(flipped, scale)) > hairPoints.get(i).maxDistance * hairPoints.get(i).maxDistance * scale * scale) {
                Vector2 dir = hairPoints.get(i).pos.cpy().sub(hairPoints.get(i - 1).getOffsettedPosition(flipped, scale)).nor();
                hairPoints.get(i).pos.set(hairPoints.get(i - 1).getOffsettedPosition(flipped, scale)).mulAdd(dir, hairPoints.get(i).maxDistance * scale);
            }
        }
    }

    @Override
    public void render() {
        for (int i = 0; i < hairPoints.size; i++) {
            HairPoint point = hairPoints.get(i);
            tmpVector.set(point.pos);
            SpongeGame.i().getShapeDrawer().filledCircle(tmpVector, (point.radius + 1) * scale, Color.BLACK);
        }

        for (int i = 0; i < hairPoints.size; i++) {
            HairPoint point = hairPoints.get(i);
            tmpVector.set(point.pos);
            SpongeGame.i().getShapeDrawer().filledCircle(tmpVector, point.radius * scale, Color.valueOf("fec62b"));
        }

    }

    public void setSource(Vector2 source) {
        this.source.set(source);
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public Vector2 getSource() {
        return source;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
