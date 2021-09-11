package byow.Core;

import byow.TileEngine.TETile;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Leaf {
    // one room should at least occupy 16 tiles
    private static final int minRoomSize = 4;                  // 4 works nice

    // one leaf should at least occupy 25 tiles
    private static final int heightLimit = 5;                  // 5 works nice
    private static final int widthLimit = 5;                   // 5 works nice

    // upper and lower bound for width / (width + height) ratio
    private static final double ratioUpperLimit = 0.6;
    private static final double ratioLowerLimit = 0.4;

    private Room room;
    private Hallway centerHallway;
    private Leaf childOne;
    private Leaf childTwo;
    private Leaf parent;
    private int height;
    private int width;
    private int xCoordinate;
    private int yCoordinate;
    private TETile[][] worldMap;
    private Random generator;
    private double ratio;                                // Ratio for width to (width + height)
    private int depth;

    public Leaf(TETile[][] worldMap, Random generator, int height, int width, int xCor, int yCor, int depth, Leaf parent) {

        this.room = null;
        this.childOne = null;
        this.childTwo = null;
        this.parent = parent;
        this.generator = generator;
        this.height = height;
        this.width = width;
        this.xCoordinate = xCor;
        this.yCoordinate = yCor;
        this.worldMap = worldMap;
        this.ratio = (double) width / (width + height);
        this.depth = depth;

    }


    /**
     * connect the center point of two child leaves if exist
     *      1. return true if connect successfully
     *      2. return false if missing child leaves
     *
     *      Case 1 (childOne no room && childTwo has room)                      - connect the center of childTwo to the center of leaf (current leaf)
     *      Case 2 (childOne no room && childTwo no room)                       - do nothing
     *      Case 3 (childOne has room && childTwo no room)                      - divide vertically
     *      Case 4 (childOne has room && childTWo has room)                               - divide randomly
     */
    public HashSet<Hallway> createCenterHallway() {
        HashSet<Hallway> allHallways = new HashSet<>();
        Leaf leafOne = this.getChildOne();
        Leaf leafTwo = this.getChildTwo();

        if (leafOne == null && leafTwo == null) {
            return allHallways;
        }

        Hallway newHallway;
        if (ishorizontalHallway(leafOne, leafTwo)) {
            newHallway = new Hallway(locateLeafCenter(leafOne), locateLeafCenter(leafTwo), "h");
        } else {
            newHallway = new Hallway(locateLeafCenter(leafOne), locateLeafCenter(leafTwo), "v");
        }
        this.setCenterHallway(newHallway);
        allHallways.add(newHallway);
        allHallways.addAll(leafOne.createCenterHallway());
        allHallways.addAll(leafTwo.createCenterHallway());
        return allHallways;
    }

    private static LinkedList<Integer> locateLeafCenter(Leaf leaf) {
        LinkedList<Integer> position = new LinkedList<>();
        position.addLast(leaf.getxCoordinate() + (leaf.getWidth() - 1) / 2);
        position.addLast(leaf.getyCoordinate() + (leaf.getHeight() - 1) / 2);
        return position;
    }

    private static boolean ishorizontalHallway(Leaf leafOne, Leaf leafTwo) {
        if (leafOne.getxCoordinate() == leafTwo.getxCoordinate()) {
            return false;
        }
        return true;
    }

    /**
     * divide the current leaf into two leaves
     *      1. return true if divide successfully
     *      2. return false if created room or larger than width/height limit
     *
     * Case 1 (width < widthLimit || height < heightLimit               - return false and do nothing
     * Case 2 (depth > 3)                                               - randomly choose between  1. return false; create room  2. return true; further divide
     * Case 3 (ratio > 0.6)                                             - divide vertically
     * Case 4 (ratio < 0.6 && ratio > 0.4)                              - divide randomly
     * Case 5 (ratio < 0.4)                                             - divide horizontally
     */
    public boolean divideLeaf() {

        if (height < heightLimit || width < widthLimit) {
            return false;
        } else if (depth > 3 && RandomUtils.uniform(generator, 0, 10) > 6) {     // depth > 3
            createRoom();
            return false;
        } else if (ratio > ratioUpperLimit) {
            divideVertical();
        } else if (ratio < ratioLowerLimit) {
            divideHorizontal();
        } else {
            if (RandomUtils.uniform(generator, 0, 2) == 1) {
                divideVertical();
            } else {
                divideHorizontal();
            }
        }
        return true;
    }

    private int calYCoordinate(int yCoordinate, int height) {
        return yCoordinate + height;
    }

    private int calXCoordinate(int xCoordinate, int width) {
        return xCoordinate + width;
    }

    private void divideHorizontal() {

        // randomly generate a height between 1 to height of the current leaf
        int childOneHeight = RandomUtils.uniform(generator, 1, height);
        while (childOneHeight > height * 0.7 || childOneHeight < height * 0.3) {
            childOneHeight = RandomUtils.uniform(generator, 1, height);
        }
        int childTwoHeight = height - childOneHeight;

        // create two leaves - the lower one is childOne & the upper one is childTwo
        this.childOne = new Leaf(worldMap, generator, childOneHeight, width, xCoordinate, yCoordinate, depth + 1, this);
        this.childTwo = new Leaf(worldMap, generator, childTwoHeight, width, xCoordinate, calYCoordinate(yCoordinate, childOneHeight), depth + 1, this);

        if (childOne.divideLeaf()) {
            childOne.divideLeaf();
        }
        if (childTwo.divideLeaf()) {
            childTwo.divideLeaf();
        }
    }

    private void divideVertical() {

        // randomly generate a height between 1 to width of the current leaf
        int childOneWidth = RandomUtils.uniform(generator, 1, width);
        while (childOneWidth > width * 0.7 || childOneWidth < width * 0.3) {
            childOneWidth = RandomUtils.uniform(generator, 1, width);
        }
        int childTwoWidth = width - childOneWidth;

        // create two leaves - the left one is childOne & the right one is childTwo
        this.childOne = new Leaf(worldMap, generator, height, childOneWidth, xCoordinate, yCoordinate, depth + 1, this);
        this.childTwo = new Leaf(worldMap, generator, height, childTwoWidth, calXCoordinate(xCoordinate, childOneWidth), yCoordinate, depth + 1, this);

        if (childOne.divideLeaf()) {
            childOne.divideLeaf();
        }
        if (childTwo.divideLeaf()) {
            childTwo.divideLeaf();
        }
    }

    public void createRoom() {
        int roomHeight = RandomUtils.uniform(generator, minRoomSize, this.height);
        int roomWidth = RandomUtils.uniform(generator, minRoomSize, this.width);

        int roomXCoordinate = xCoordinate + (width - roomWidth) / 2;
        int roomYCoordinate = yCoordinate + (height - roomHeight) / 2;

        /*int roomXCoordinate = RandomUtils.uniform(generator, this.xCoordinate, this.xCoordinate + this.width - roomWidth);
        int roomYCoordinate = RandomUtils.uniform(generator, this.yCoordinate, this.yCoordinate + this.height - roomHeight);*/
        Room newRoom = new Room(roomHeight, roomWidth, roomXCoordinate, roomYCoordinate);
        this.room = newRoom;
    }

    public void setCenterHallway(Hallway hallway) {
        this.centerHallway = hallway;
    }

    public Room getRoom() {
        return room;
    }

    public Leaf getChildOne() {
        return childOne;
    }

    public Leaf getParent() {
        return parent;
    }

    public Leaf getChildTwo() {
        return childTwo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

}
