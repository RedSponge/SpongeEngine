package com.redsponge.sponge.test;

import java.util.Arrays;

public class Level {

    private RoomTile[][] tiles;
    private int springCount, pxCount, pyCount, nxCount, nyCount;

    public Level(RoomTile[][] tiles, int springCount, int pxCount, int pyCount, int nxCount, int nyCount) {
        this.tiles = tiles;
        this.springCount = springCount;
        this.pxCount = pxCount;
        this.pyCount = pyCount;
        this.nxCount = nxCount;
        this.nyCount = nyCount;
    }

    public RoomTile[][] getTiles() {
        return tiles;
    }

    public void setTiles(RoomTile[][] tiles) {
        this.tiles = tiles;
    }

    public int getSpringCount() {
        return springCount;
    }

    public void setSpringCount(int springCount) {
        this.springCount = springCount;
    }

    public int getPxCount() {
        return pxCount;
    }

    public void setPxCount(int pxCount) {
        this.pxCount = pxCount;
    }

    public int getPyCount() {
        return pyCount;
    }

    public void setPyCount(int pyCount) {
        this.pyCount = pyCount;
    }

    public int getNxCount() {
        return nxCount;
    }

    public void setNxCount(int nxCount) {
        this.nxCount = nxCount;
    }

    public int getNyCount() {
        return nyCount;
    }

    public void setNyCount(int nyCount) {
        this.nyCount = nyCount;
    }

    @Override
    public String toString() {
        return "Level{" +
                "tiles=" + Arrays.toString(tiles) +
                ", springCount=" + springCount +
                ", pxCount=" + pxCount +
                ", pyCount=" + pyCount +
                ", nxCount=" + nxCount +
                ", nyCount=" + nyCount +
                '}';
    }
}
