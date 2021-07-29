package com.redsponge.sponge.test.presentation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

public class MapEntity extends Entity {

    private TiledMap map;
    private OrthogonalTiledMapRenderer tmr;
    private boolean isFirst;

    public MapEntity() {
        super(new Vector2(0, 0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        isFirst = true;
        map = new TmxMapLoader().load("test/maps/demo.tmx");
        tmr = new OrthogonalTiledMapRenderer(map, SpongeGame.i().getBatch());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(isFirst) {
            Array<RectangleMapObject> arr = map.getLayers().get("Collidables").getObjects().getByType(RectangleMapObject.class);
            for (int i = 0; i < arr.size; i++) {
                Rectangle r = arr.get(i).getRectangle();
                getScene().add(new PSolid(new Vector2(r.x, r.y), new Hitbox(0, 0, (int) r.width, (int) r.height)));
            }
            isFirst = false;
        }
    }

    @Override
    public void render() {
        SpongeGame.i().getBatch().end();
        SpongeGame.i().getBatch().setColor(0.78f, 0.78f, 0.78f, 1);
        tmr.setView((OrthographicCamera) getScene().getViewport().getCamera());
        tmr.render();

        SpongeGame.i().getBatch().setColor(1, 1, 1, 1);
        SpongeGame.i().getBatch().setProjectionMatrix(getScene().getViewport().getCamera().combined);
        SpongeGame.i().getBatch().begin();

        super.render();
    }

    @Override
    public void removed() {
        tmr.dispose();
        map.dispose();
        super.removed();
    }
}
