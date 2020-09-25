package com.redsponge.sponge.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Connections {

    private static final HashMap<String, Set<Connection>> connections = new HashMap<>();
    private static final HashMap<String, Boolean> cachedResults = new HashMap<>();

    public static void prepCache() {
        for (String s : connections.keySet()) {
            cachedResults.put(s, isActiveNoCache(s));
        }
    }

    public static boolean isActive(String name) {
        return cachedResults.getOrDefault(name, false);
    }

    public static boolean isActiveNoCache(String name) {
        for (Connection connection : connections.get(name)) {
            if(!connection.isOn()) return false;
        }
        return true;
    }

    public static void registerConnection(String name, Connection connection, boolean updateCache) {
        if(connections.containsKey(name)) {
            connections.get(name).add(connection);
        } else {
            Set<Connection> l = new HashSet<>();
            l.add(connection);
            connections.put(name, l);
        }
        if(updateCache) {
            cachedResults.put(name, isActiveNoCache(name));
        }
    }

    public static void unregisterConnection(String name, Connection connection) {
        if(connections.containsKey(name)) {
            connections.get(name).remove(connection);
            if(connections.get(name).size() == 0) {
                cachedResults.put(name, false);
            }
        }
    }

}
