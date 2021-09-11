package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        int w = 10;
        int h = 10;
        ter.initialize(w, h);
        TETile[][] world = new TETile[w][h];
        blankWorld(w, h, world);
        addHexagon(3, world, 1, 1);
        ter.renderFrame(world);
    }

    private static void blankWorld(int width, int height, TETile[][] world) {
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void addHexagon(int side, TETile[][] world, int cornerW, int cornerH) {
        upper(side - 1, side, side, world, cornerH, cornerW);
        lower(0, side * 3 - 2, side, world, cornerH + side, cornerW);
    }

    private static void upper(int blank, int content, int side, TETile[][] world, int line, int start) {
        for (int j = 0; j < content; j ++) {
            world[start + blank + j][line] = Tileset.WALL;
        }
        if (content != side * 3 - 2) {
            upper(blank - 1, content + 2, side, world, line + 1, start);
        }
    }

    private static void lower(int blank, int content, int side, TETile[][] world, int line, int start) {
        for (int j = 0; j < content; j ++) {
            world[start + blank + j][line] = Tileset.WALL;
        }
        if (content != side) {
            lower(blank + 1, content - 2, side, world, line + 1, start);
        }
    }

    public static void tesselation(int startW, int startH, int dimension, int side, TETile[][] world) {
        tesselationAdd(startW, startH, dimension, side, 0, world);
    }

    private static void tesselationAdd(int startW, int startH, int dimension, int side, int line, TETile[][] world) {
        int avgL = (side - 1) * 2;
        int totalHeight = dimension * 3 - 2;
        if (line == 0) {
            addHexagon(side, world, startW + avgL * (dimension - 1), startH);
        }
        else if (line == totalHeight - 1) {
            addHexagon(side, world, startW + avgL * (dimension - 1), startH);
        } else if (line >= dimension - 1 || line <= (dimension - 1) * 2){
        }
    }
}
