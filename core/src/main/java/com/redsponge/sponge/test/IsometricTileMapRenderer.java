package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.components.DrawnComponent;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.rendering.BloomEffect;
import com.redsponge.sponge.screen.Scene;

import java.util.function.Function;

public class IsometricTileMapRenderer extends Entity {

    private Texture tileTexture;

    private LevelSimulator levelSimulator;

    private Texture arrowPy, arrowNy, arrowPx, arrowNx;
    private Animation<TextureRegion> portal;

    private TextureRegion legoTexture;

    private Array<Function<Float, Float>> goDownFunctions;
    private Array<Function<Float, Float>> goUpFunctions;

    private DrawnComponent drawn;
    private float time;
    private TextureRegion trampolineTexture, crystalTexture;

    private TextureRegion winTexture;

    public IsometricTileMapRenderer(Vector2 pos, LevelSimulator levelSimulator) {
        super(pos);
        this.levelSimulator = levelSimulator;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        tileTexture = scene.getAssets().get("tile.png");
        arrowPx = scene.getAssets().get("arrow_px.png");
        arrowPy = scene.getAssets().get("arrow_py.png");
        arrowNx = scene.getAssets().get("arrow_nx.png");
        arrowNy = scene.getAssets().get("arrow_ny.png");
        portal = scene.getAssets().getAnimationGroup("portal").get("ground_portal").getBuiltAnimation();
        legoTexture = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("lego");
        trampolineTexture = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("trampoline");
        crystalTexture = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("crystal");
        winTexture = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("pillow_down");
        genFunctions();

        drawn = new DrawnComponent(true, true, scene.getAssets().<Texture>get("bg.png"));
        drawn.setX(0).setY(0).setPositionPolicy(DrawnComponent.PositionPolicy.USE_XY);
        add(drawn);

        scene.getRenderingPipeline().getEffect(BloomEffect.class).addBloomRender(() -> {
            Vector2 tmp = new Vector2();
            int size = 7;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    RoomTile tile = levelSimulator.getRoom()[i][j];
                    getPositionOfIndex(j, i, tmp);
                    if(tile instanceof PortalTile) {
                        PortalTile p = (PortalTile) tile;
                        SpongeGame.i().getBatch().setColor(p.getColour());
                        SpongeGame.i().getBatch().draw(portal.getKeyFrame(time), tmp.x + 4, tmp.y + 2);
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    RoomTile tile = levelSimulator.getRoom()[i][j];
                    getPositionOfIndex(j, i, tmp);
                    if(tile instanceof OrbGiveTile) {
                        SpongeGame.i().getBatch().setColor(1, 1, 1, 0.5f);
                        float power = (float) Math.sin(time + (i + 1) * (j + 1));
                        SpongeGame.i().getBatch().draw(crystalTexture, tmp.x + 8+4, tmp.y + +2+4+4 + power * 4);
                    }
                }
            }
        });
    }

    private void genFunctions() {
        goDownFunctions = new Array<>();
        goUpFunctions = new Array<>();
        for(int i = -80; i <= 60; i += 18) {
            int finalI = i;
            goDownFunctions.add((x) -> x / 2f + finalI);
            goUpFunctions.add((x) -> -x / 2f + finalI + 160);
        }
    }

    public Vector2 getPositionOfIndex(int x, int y, Vector2 output) {
        output.x = x * 18 - y * 18 + getX();
        output.y = x * 9 + y * 9 + getY();
        return output;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;
    }

    @Override
    public void render() {
        super.render();
        int size = 7;
        Vector2 tmp = new Vector2();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                RoomTile tile = levelSimulator.getRoom()[i][j];
//                getPositionOfIndex(j, i, tmp);
//                if(!(tile instanceof EmptyTile)) {
//                    SpongeGame.i().getBatch().setColor(levelSimulator.getRoom()[i][j].getDebugColor());
//                    SpongeGame.i().getBatch().draw(tileTexture, tmp.x, tmp.y);
//                }
//            }
//        }
        SpongeGame.i().getBatch().setColor(Color.WHITE);

        for (int i = size - 1; i >= 0; i--) {
            for (int j = size - 1; j >= 0; j--) {
                RoomTile tile = levelSimulator.getRoom()[i][j];
                getPositionOfIndex(j, i, tmp);
                if(tile instanceof DirectionSetTile) {
                    SpongeGame.i().getBatch().setColor(Color.WHITE);
                    DirectionSetTile d = (DirectionSetTile) tile;
                    Vector2 dir = d.getDirection();
                    Texture toUse = dir.y == 0 ? (dir.x > 0 ? arrowPx : arrowNx) : (dir.y > 0 ? arrowPy : arrowNy);
                    SpongeGame.i().getBatch().draw(toUse, tmp.x + 4, tmp.y + 2);
                } else if(tile instanceof PortalTile) {
                    PortalTile p = (PortalTile) tile;
                    SpongeGame.i().getBatch().setColor(p.getColour());
                    SpongeGame.i().getBatch().draw(portal.getKeyFrame(time), tmp.x + 4, tmp.y + 2);
                } else if(tile instanceof DeathTile) {
                    SpongeGame.i().getBatch().setColor(Color.WHITE);
                    SpongeGame.i().getBatch().draw(legoTexture, tmp.x + 4, tmp.y + 2);
                } else if(tile instanceof SkipTile) {
                    SpongeGame.i().getBatch().setColor(Color.WHITE);
                    SpongeGame.i().getBatch().draw(trampolineTexture, tmp.x + 2, tmp.y + 2+2);
                } else if(tile instanceof WinTile) {
                    SpongeGame.i().getBatch().setColor(Color.WHITE);
                    SpongeGame.i().getBatch().draw(winTexture, tmp.x + 4, tmp.y + 2);
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RoomTile tile = levelSimulator.getRoom()[i][j];
                getPositionOfIndex(j, i, tmp);
                if(tile instanceof OrbGiveTile) {
                    SpongeGame.i().getBatch().setColor(Color.WHITE);
                    float power = (float) Math.sin(time + (i + 1) * (j + 1));
                    SpongeGame.i().getBatch().draw(crystalTexture, tmp.x + 8+4, tmp.y + +2+4+4 + power * 4);
                }
            }
        }


    }

    public Vector2 getIndexByPoint(Vector2 point, Vector2 output) {
        int funcIdxX = 0;
        while (funcIdxX < goDownFunctions.size && goDownFunctions.get(funcIdxX).apply(point.x) < point.y) {
            funcIdxX++;
        }

        int funcIdxY = 0;
        while (funcIdxY < goUpFunctions.size && goUpFunctions.get(funcIdxY).apply(point.x) < point.y ) {
            funcIdxY++;
        }
        return output.set( funcIdxY - 1, funcIdxX - 1);
    }

    private void renderFunctions(Array<Function<Float, Float>> function) {
        for (Function<Float, Float> func : function) {
            for(float x = 0; x < 320; x++) {
                SpongeGame.i().getShapeDrawer().filledCircle(x, func.apply(x), 1, Color.BLACK);
            }
        }
    }

    public LevelSimulator getLevel() {
        return levelSimulator;
    }
}
