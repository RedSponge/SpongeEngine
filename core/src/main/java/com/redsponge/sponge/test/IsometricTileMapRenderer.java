package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;

public class IsometricTileMapRenderer extends Entity {

    private Texture tileTexture;

    private LevelSimulator levelSimulator;

    public IsometricTileMapRenderer(Vector2 pos, LevelSimulator levelSimulator) {
        super(pos);
        this.levelSimulator = levelSimulator;
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        tileTexture = scene.getAssets().get("tile.png");
    }

    public void drawLine(int startX, int y, int amount) {
        for (int i = 0; i < amount; i++) {
            SpongeGame.i().getBatch().draw(tileTexture, startX + 14 * i, y);
        }
    }

    public Vector2 getPositionOfIndex(int x, int y, Vector2 output) {
        output.x = x * 14 - y * 14 + getX();
        output.y = x * 7 + y * 7 + getY();
        return output;
    }

    @Override
    public void render() {
        super.render();
        int size = 7;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                SpongeGame.i().getBatch().setColor(levelSimulator.getRoom()[i][j].getDebugColor());
                SpongeGame.i().getBatch().draw(tileTexture, j * 14 - 14 * i + getX(), j * 7 + i * 7 + getY());
            }
        }
    }

    public LevelSimulator getLevel() {
        return levelSimulator;
    }
}
