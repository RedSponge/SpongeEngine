package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

import java.util.HashMap;

public class IsometricTileMapRenderer extends Entity {

    private Texture tileTexture;

    private LevelSimulator levelSimulator;

    private Texture arrowPy, arrowNy, arrowPx, arrowNx;

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
    }

    public void drawLine(int startX, int y, int amount) {
        for (int i = 0; i < amount; i++) {
            SpongeGame.i().getBatch().draw(tileTexture, startX + 14 * i, y);
        }
    }

    public Vector2 getPositionOfIndex(int x, int y, Vector2 output) {
        output.x = x * 18 - y * 18 + getX();
        output.y = x * 9 + y * 9 + getY();
        return output;
    }

    @Override
    public void render() {
        super.render();
        int size = 7;
        Vector2 tmp = new Vector2();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RoomTile tile = levelSimulator.getRoom()[i][j];
                getPositionOfIndex(j, i, tmp);
                SpongeGame.i().getBatch().setColor(levelSimulator.getRoom()[i][j].getDebugColor());
                SpongeGame.i().getBatch().draw(tileTexture, tmp.x, tmp.y);
            }
        }
        SpongeGame.i().getBatch().setColor(Color.WHITE);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RoomTile tile = levelSimulator.getRoom()[i][j];
                getPositionOfIndex(j, i, tmp);
                if(tile instanceof DirectionSetTile) {
                    DirectionSetTile d = (DirectionSetTile) tile;
                    Vector2 dir = d.getDirection();
                    Texture toUse = dir.y == 0 ? (dir.x > 0 ? arrowPx : arrowNx) : (dir.y > 0 ? arrowPy : arrowNy);
                    SpongeGame.i().getBatch().draw(toUse, tmp.x + 4, tmp.y + 2);
                }
            }
        }


    }

    public LevelSimulator getLevel() {
        return levelSimulator;
    }
}
