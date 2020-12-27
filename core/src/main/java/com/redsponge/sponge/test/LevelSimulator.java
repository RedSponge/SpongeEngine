package com.redsponge.sponge.test;

import com.badlogic.gdx.math.Vector2;

public class LevelSimulator {

    private final int ROOM_SIZE = 7;
    private RoomTile[][] room;
    private Vector2 playerPos;
    private Vector2 playerVel;
    private boolean isPlayerProtected;

    public LevelSimulator() {
        playerPos = new Vector2();
        playerVel = new Vector2(1, 0);
        room = new RoomTile[ROOM_SIZE][ROOM_SIZE];
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[i].length; j++) {
                room[i][j] = new EmptyTile();
            }
        }
    }

    public void loadLevel(RoomTile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, room[i], 0, tiles[i].length);
        }
    }

    public void setRoomObject(RoomTile obj, int x, int y) {
        room[y][x] = obj;
    }

    public Vector2 getPlayerPos() {
        return playerPos;
    }

    public Vector2 getPlayerVel() {
        return playerVel;
    }

    public RoomTile[][] getRoom() {
        return room;
    }

    public void progressPlayer() {
        int targetX = (int) (playerPos.x + playerVel.x);
        int targetY = (int) (playerPos.y + playerVel.y);
        if(targetX < 0 || targetX >= ROOM_SIZE || targetY < 0 || targetY >= ROOM_SIZE) {
            playerVel.scl(-1);
        } else {
            RoomTile nextObject = room[targetY][targetX];
            if (nextObject.canPlayerEnter(this)) {
                playerPos.x = targetX;
                playerPos.y = targetY;
            }
            nextObject.onPlayerEnter(this);
        }
    }

    public Vector2 findTile(RoomTile other) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[i].length; j++) {
                if(room[i][j] == other) return new Vector2(j, i);
            }
        }
        return null;
    }

    public void reset() {
        playerPos.set(0, 0);
        playerVel.set(1, 0);
    }

    public void setPlayerProtected(boolean playerProtected) {
        isPlayerProtected = playerProtected;
    }

    public boolean isPlayerProtected() {
        return isPlayerProtected;
    }
}
