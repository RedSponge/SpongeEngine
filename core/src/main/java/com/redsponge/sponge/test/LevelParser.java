package com.redsponge.sponge.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        put(".", WinTile::new);
    }};

    public static Level parseLevel(String text) {
        HashMap<String, int[]> portalPairs = new HashMap<>();
        int numPortals = 0;
        RoomTile[][] room = new RoomTile[7][7];
//        Level levelSim = new LevelSimulator();
        for (int i = 0; i < 49; i++) {
            room[i / 7][i % 7] = new EmptyTile();
        }
        String[] lines = text.split("\n");
        for(int y = 0; y < 7; y++) {
            String[] partss = lines[y].split(",");
//            System.out.println(partss[6] + " " + partss.length + " " + lines[y]);
            for (int x = 0; x < partss.length; x++) {
                String part = partss[x].trim();
                if(part.isEmpty()) part = " ";
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
                            createPortalPair(room, pp, numPortals++);
                            portalPairs.remove(part);
                        }
                    } else {
                        System.out.println("Skipping '" + part + "'");
                    }
                } else {
                    room[6 - y][x] = generators.get(part).generate();
                }
            }
        }
        String[] counts = lines[7].split(" ");
        List<Integer> ints = Arrays.stream(counts).map(Integer::parseInt).collect(Collectors.toList());
        System.out.println(ints);
        return new Level(room, ints.get(4), ints.get(2), ints.get(3), ints.get(0), ints.get(1));
    }

    private static void createPortalPair(RoomTile[][] room, int[] pp, int idx) {
        PortalTile a = new PortalTile(PortalTile.colours[idx]);
        PortalTile b = new PortalTile(PortalTile.colours[idx], a);
        a.setOther(b);

        room[pp[1]][pp[0]] = a;
        room[pp[3]][pp[2]] = b;
    }

}
