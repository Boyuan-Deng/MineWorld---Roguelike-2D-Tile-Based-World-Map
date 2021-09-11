package byow.Core;

import byow.TileEngine.*;

import java.util.HashSet;
import java.util.Random;

public class WorldGenerator {

    private static final int WORLDWIDTH = 80;
    private static final int WORLDHEIGHT = 30;
    private static final Engine THEENGINE = new Engine();

    private Random generator;

    public static void main(String[] args) {
        TETile[][] world = THEENGINE.interactWithInputString(args[0]);
        for (int i = 0; i < world[0].length; i ++) {
            for (int j = 0; j < world.length; j++) {
                System.out.print(world[j][i].character());
            }
            System.out.println();
        }

        /**TERenderer ter = new TERenderer();
        long seed = 12345678;
        ter.initialize(WORLDWIDTH, WORLDHEIGHT);
        TETile[][] worldMap = new TETile[WORLDWIDTH][WORLDHEIGHT];
        initialization(WORLDWIDTH, WORLDHEIGHT, seed, worldMap);
        ter.renderFrame(worldMap);
        */
    }

    public static void initialization(int width, int height, long seed, TETile[][] worldMap) {
        Random generator = new Random(seed);
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                worldMap[i][j] = Tileset.NOTHING;
            }
        }
        Leaf initial = new Leaf(worldMap, generator, height, width, 0, 0, 0, null);
        initial.divideLeaf();
        HashSet<Hallway> allHallways = initial.createCenterHallway();
        HashSet<Room> rooms = findRoom(initial);
        for (Room r : rooms) {
            drawRoom(r, worldMap);
        }
        for (Hallway h : allHallways) {
            drawHallway(h, worldMap);
        }
    }



    public static HashSet<Room> findRoom(Leaf leaf) {
        HashSet<Room> rooms = new HashSet<>();
        if (leaf.getRoom() != null) {
            rooms.add(leaf.getRoom());
            return rooms;
        }

        if (leaf.getChildOne() != null) {
            rooms.addAll(findRoom(leaf.getChildOne()));
        }

        if (leaf.getChildTwo() != null) {
            rooms.addAll(findRoom(leaf.getChildTwo()));
        }
        return rooms;

    }

    public static void setNeighborsToWall(int xCor, int yCor, TETile[][] worldMap) {

        for (int i = -1; i < 2; i ++) {
            if (xCor != 0 && yCor != 0) {
                if (worldMap[xCor + i][yCor - 1].description().equals("nothing")) {
                    worldMap[xCor + i][yCor - 1] = Tileset.WALL;
                }
                if (worldMap[xCor + i][yCor + 1].description().equals("nothing")) {
                    worldMap[xCor + i][yCor + 1] = Tileset.WALL;
                }
            } else {
                worldMap[xCor][yCor] = Tileset.WALL;
            }
        }


        for (int i = -1; i < 2; i ++) {
            if (xCor != 0 && yCor != 0) {
                if (worldMap[xCor - 1][yCor + i].description().equals("nothing")) {
                    worldMap[xCor - 1][yCor + i] = Tileset.WALL;
                }
                if (worldMap[xCor + 1][yCor + i].description().equals("nothing")) {
                    worldMap[xCor + 1][yCor + i] = Tileset.WALL;
                }
            } else {
                worldMap[xCor][yCor] = Tileset.WALL;
            }
        }
    }

    public static void drawHallway(Hallway hallway, TETile[][] worldMap) {

        int xStart = hallway.getxStart();
        int xEnd = hallway.getxEnd();
        int yStart = hallway.getyStart();
        int yEnd = hallway.getyEnd();
        if (hallway.getOrientation().equals("h")) {
            for (int i = xStart; i <= xEnd; i++) {
                worldMap[i][yStart] = Tileset.FLOOR;
                setNeighborsToWall(i, yStart, worldMap);
            }
        } else {
            for (int j = yStart; j <= yEnd; j++) {
                worldMap[xStart][j] = Tileset.FLOOR;
                setNeighborsToWall(xStart, j, worldMap);
            }
        }

    }

    public static void drawRoom(Room room, TETile[][] worldMap) {
        // default by filling all the room size by the floor
        int height = room.getHeight();
        int width = room.getWidth();
        int xCor = room.getXCoordinate();
        int yCor = room.getYCoordinate();
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                worldMap[xCor + j][yCor + i] = Tileset.FLOOR;
            }
        }

        // add the wall for the width sides(x cor direction)
        for (int i = 0; i < width; i ++) {
            worldMap[xCor + i][yCor] = Tileset.WALL;
            worldMap[xCor + i][yCor + height - 1] = Tileset.WALL;
        }

        // add the wall for the height sides(y cor direction)
        for (int i = 0; i < height; i ++) {
            worldMap[xCor][yCor + i] = Tileset.WALL;
            worldMap[xCor + width - 1][yCor + i] = Tileset.WALL;
        }
    }

    public Random getGenerator() {
        return generator;
    }

    public void setGenerator(Random generator) {
        this.generator = generator;
    }

}
