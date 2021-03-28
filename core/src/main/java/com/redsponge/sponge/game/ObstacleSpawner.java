package com.redsponge.sponge.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Logger;
import com.redsponge.sponge.util.UJava;
import com.redsponge.sponge.util.UMath;

import java.util.ArrayList;
import java.util.List;

public class ObstacleSpawner extends Entity {

    private DelayedRemovalArray<Obstacle> obstacles;
    private float time;
    private GameTimeManager timeManager;

    public ObstacleSpawner() {
        super(new Vector2());
        obstacles = new DelayedRemovalArray<>();
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        timeManager = scene.all(new ArrayList<>(), GameTimeManager.class).get(0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        time += delta;

        float spawnTime = UMath.lerp(5, 2, timeManager.getCompletedPercent());
        if(time > spawnTime) {
            time -= spawnTime;
            spawnObstacle();
        }
        cleanObstacles();
    }

    private void cleanObstacles() {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < obstacles.size; i++) {
            if(obstacles.get(i).getPositionf().y < -100) {
                ints.add(i);
                Logger.info(this, "Removing Obstacle", i, obstacles.get(i).getPositionf());
                obstacles.get(i).removeSelf();
            }
        }
        for (Integer index : ints) {
            obstacles.removeIndex(index);
        }
    }

    private void spawnObstacle() {
        Obstacle o = new Obstacle(new Vector2(MathUtils.random(getScene().getWidth()), getScene().getHeight()), UJava.randomValue(Constants.OBSTACLE_OPTIONS));
        getScene().add(o);
        obstacles.add(o);
    }
}
