package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.function.Function;

public class LevelParser {


    private static HashMap<String, Generator<RoomTile>> generators = new HashMap<String, Generator<RoomTile>>() {{
        put(" ", EmptyTile::new);
        put(">", () -> new DirectionSetTile(new Vector2(1, 0)));
        put("<", () -> new DirectionSetTile(new Vector2(-1, 0)));
        put("^", () -> new DirectionSetTile(new Vector2(0, 1)));
        put("v", () -> new DirectionSetTile(new Vector2(0, -1)));
        put("S", SkipTile::new);
        put("|", ObstacleTile::new);
        put("X", DeathTile::new);
        put("+", OrbGiveTile::new);
    }};

    public static LevelSimulator parseLevel(String text) {
        HashMap<String, int[]> portalPairs = new HashMap<>();
        int numPortals = 0;
        LevelSimulator levelSim = new LevelSimulator();
        String[] lines = text.split("\n");
        for(int y = 0; y < 7; y++) {
            System.out.println(lines[y]);
            String[] partss = lines[y].split(",");
            System.out.println(partss.length);
            for (int x = 0; x < partss.length; x++) {
                String part = partss[x];
                if(generators.get(part) == null) {
                    if('A' <= part.charAt(0) && part.charAt(0) <= 'Z') {
                        if (portalPairs.get(part) == null) {
                            System.out.println("Starting Portal '" + part + "'");
                            portalPairs.put(part, new int[]{x, 6 - y, 0, 0});
                        } else {
                            int[] pp = portalPairs.get(part);
                            System.out.println("Creating Portal '" + part + "'");
                            pp[2] = x;
                            pp[3] = 6 - y;
                            createPortalPair(levelSim, pp, numPortals++);
                            portalPairs.remove(part);
                        }
                    } else {
                        System.out.println("Skipping '" + part + "'");
                    }
                    continue;
                }
                levelSim.setRoomObject(generators.get(part).generate(), x, 6 - y);
            }
        }
        return levelSim;
    }

    private static void createPortalPair(LevelSimulator levelSim, int[] pp, int idx) {
        PortalTile a = new PortalTile(PortalTile.colours[idx]);
        PortalTile b = new PortalTile(PortalTile.colours[idx], a);
        a.setOther(b);

        levelSim.setRoomObject(a, pp[0], pp[1]);
        levelSim.setRoomObject(b, pp[2], pp[3]);
    }

}
