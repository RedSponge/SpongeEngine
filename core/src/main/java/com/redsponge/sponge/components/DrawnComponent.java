package com.redsponge.sponge.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Component;

public class DrawnComponent extends Component {

    public enum PositionPolicy {
        USE_ENTITY,
        USE_XY
    }

    public enum SizePolicy {
        USE_ENTITY,
        USE_REGION,
        USE_WH
    }

    private TextureRegion rendered;
    private float offsetX = 0;
    private float offsetY = 0;

    private PositionPolicy positionPolicy = PositionPolicy.USE_ENTITY;
    private float x = 0;
    private float y = 0;

    private SizePolicy sizePolicy = SizePolicy.USE_REGION;
    private float width = 0;
    private float height = 0;

    private boolean flipped = false;
    private float rotation = 0;
    private float originX = 0;
    private float originY = 0;
    private float scaleX = 1;
    private float scaleY = 1;

    public DrawnComponent(boolean active, boolean visible, TextureRegion rendered) {
        super(active, visible);
        this.rendered = rendered;
    }

    public DrawnComponent(boolean active, boolean visible, Texture rendered) {
        this(active, visible, new TextureRegion(rendered));
    }


    @Override
    public void begin() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        if(rendered == null) return;
        float x = getCalculatedX();
        float y = getCalculatedY();
        float width = getCalculatedWidth();
        float height = getCalculatedHeight();
        SpongeGame.i().getBatch().draw(rendered, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }

    public float getCalculatedX() {
        float xVal;
        switch (positionPolicy) {
            case USE_ENTITY:
                xVal = getEntity().getX();
                break;
            case USE_XY:
                xVal = x;
                break;
            default:
                throw new RuntimeException("PositionPolicy is undefined! Value:'" + positionPolicy + "'");
        }
        return xVal + offsetX;
    }

    public float getCalculatedY() {
        float yVal;
        switch (positionPolicy) {
            case USE_ENTITY:
                yVal = getEntity().getY();
                break;
            case USE_XY:
                yVal = y;
                break;
            default:
                throw new RuntimeException("PositionPolicy is undefined! Value:'" + positionPolicy + "'");
        }
        return yVal + offsetY;
    }

    public float getCalculatedWidth() {
        switch (sizePolicy) {
            case USE_ENTITY:
                return getEntity().getWidth();
            case USE_REGION:
                return rendered.getRegionWidth();
            case USE_WH:
                return width;
        }
        throw new RuntimeException("SizePolicy is undefined! Value:'" + sizePolicy + "'");
    }

    public float getCalculatedHeight() {
        switch (sizePolicy) {
            case USE_ENTITY:
                return getEntity().getHeight();
            case USE_REGION:
                return rendered.getRegionHeight();
            case USE_WH:
                return height;
        }
        throw new RuntimeException("SizePolicy is undefined! Value:'" + sizePolicy + "'");
    }

    public TextureRegion getRendered() {
        return rendered;
    }

    public DrawnComponent setRendered(TextureRegion rendered) {
        this.rendered = rendered;
        return this;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public DrawnComponent setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public DrawnComponent setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public DrawnComponent setFlipped(boolean flipped) {
        this.flipped = flipped;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public DrawnComponent setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getOriginX() {
        return originX;
    }

    public DrawnComponent setOriginX(float originX) {
        this.originX = originX;
        return this;
    }

    public float getOriginY() {
        return originY;
    }

    public DrawnComponent setOriginY(float originY) {
        this.originY = originY;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public DrawnComponent setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public DrawnComponent setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getX() {
        return x;
    }

    public DrawnComponent setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public DrawnComponent setY(float y) {
        this.y = y;
        return this;
    }

    public float getScaleX() {
        return scaleX;
    }

    public DrawnComponent setScaleX(float scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public float getScaleY() {
        return scaleY;
    }

    public DrawnComponent setScaleY(float scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    public PositionPolicy getPositionPolicy() {
        return positionPolicy;
    }

    public DrawnComponent setPositionPolicy(PositionPolicy positionPolicy) {
        this.positionPolicy = positionPolicy;
        return this;
    }

    public SizePolicy getSizePolicy() {
        return sizePolicy;
    }

    public DrawnComponent setSizePolicy(SizePolicy sizePolicy) {
        this.sizePolicy = sizePolicy;
        return this;
    }
}
