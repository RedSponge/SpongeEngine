package com.redsponge.sponge.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.physics.PSolid;
import com.redsponge.sponge.screen.Scene;
import com.redsponge.sponge.util.Hitbox;

import java.util.ArrayList;
import java.util.List;

public class MapManager extends Entity {

    private TiledMap map;

    private List<Entity> entities;
    private List<Entity> addedEntities;

    private TiledMapRenderer tmr;

    public MapManager() {
        super(new Vector2());
    }

    public void load(String file) {
        if(map != null) {
            map.dispose();
            map = null;
        }
        map = new TmxMapLoader().load(file);
        tmr = new OrthogonalTiledMapRenderer(map, SpongeGame.i().getBatch());
        entities = new ArrayList<>();
        addedEntities = new ArrayList<>();
    }

    private void parseMap() {
        for (RectangleMapObject solidRMO : map.getLayers().get("Solids").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle r = solidRMO.getRectangle();
            PSolid solid = new PSolid(new Vector2(r.x, r.y), new Hitbox(0, 0, (int) r.width, (int) r.height));
            entities.add(solid);
        }
        MapObjects elements = map.getLayers().get("Elements").getObjects();
        for (RectangleMapObject rmo : elements.getByType(RectangleMapObject.class)) {
            Rectangle r = rmo.getRectangle();
            String type = rmo.getProperties().get("type", String.class);
            switch (type) {
                case "steam":
                    entities.add(new SteamColumn(new Vector2(r.x, r.y), (int) r.width, (int) r.height));
                    break;
                case "ice_player_spawn": {
                    IcePlayer p = new IcePlayer(new Vector2(r.x, r.y));
                    ((GameScene)getScene()).setPlayer(p);
                    entities.add(p);
                } break;

            }
        }
        addedEntities.addAll(entities);
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        if(map != null) {
            parseMap();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Entity solid : addedEntities) {
            getScene().add(solid);
        }
        addedEntities.clear();
    }

    @Override
    public void render() {
        super.render();
        SpongeGame.i().getBatch().end();
        tmr.setView((OrthographicCamera) getScene().viewport.getCamera());
        tmr.render();
        SpongeGame.i().getBatch().begin();
    }

    public int getMapHeight() {
        return map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);
    }
}
