package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;

public class TestRoomRenderer extends Entity {

    private LevelSimulator levelSim;

    public TestRoomRenderer(LevelSimulator simulator) {
        super(new Vector2());
        this.levelSim = simulator;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            levelSim.progressPlayer();
        }
    }

    @Override
    public void render() {
        super.render();
        RoomTile[][] objects = levelSim.getRoom();
        for (int y = 0; y < objects.length; y++) {
            for (int x = 0; x < objects[y].length; x++) {
                SpongeGame.i().getShapeDrawer().filledRectangle(x * 16, y * 16, 16, 16, objects[y][x].getDebugColor());
            }
        }
        SpongeGame.i().getShapeDrawer().filledRectangle(levelSim.getPlayerPos().x * 16, levelSim.getPlayerPos().y * 16, 16, 16, Color.GREEN);
    }
}
