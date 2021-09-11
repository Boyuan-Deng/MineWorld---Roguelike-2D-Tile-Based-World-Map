package byow.Core;

import java.util.LinkedList;

public class Hallway {
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private String orientation;

    public Hallway(LinkedList<Integer> centerA, LinkedList<Integer> centerB, String orientation) {
        this.xStart = centerA.get(0);
        this.xEnd = centerB.get(0);
        this.yStart = centerA.get(1);
        this.yEnd = centerB.get(1);
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }

    public int getxStart() {
        return xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public int getyEnd() {
        return yEnd;
    }
}