package byow.Core;

import afu.org.checkerframework.checker.oigj.qual.World;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 60);
    public static final Font NORMAL_FONT = new Font("Serif", Font.PLAIN, 20);

    private TETile[][] world;
    private WorldGenerator map;
    private Long seed;
    private String existingInput;
    private boolean gameStart = false;
    private boolean gameOver = false;
    private boolean quitEnabled = false;

    private HashSet<Character> validInput =
            new HashSet<>(Arrays.asList('w', 'a', 's', 'd', 'A', 'W', 'S', 'D', 'L',
                    'l', 'Q', 'q', 'N', 'n', ':'));
    private HashSet<Character> validNumberInput =
            new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9',
                    '0'));

    public static void main(String[] args) {
        Engine engine = new Engine();
        TERenderer ter = new TERenderer();
/*      TETile[][] blankWorld = new TETile[WIDTH][HEIGHT];
        WorldGenerator testMap = new WorldGenerator(123456, blankWorld);
        TETile[][] testWorld = testMap.getWorld();*/
        engine.saveWorld("N123456S:awsdds:Q");
        TETile[][] testWorld = engine.loadSavedWorld();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(testWorld);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        String seedString = "";
        displayMenu();

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            if (!validInput.contains(nextKey)) {
                continue;
            }
            if (!gameStart) {
                if (nextKey == 'N' || nextKey == 'n') {
                    inputSource = new KeyboardInputSource();
                    askingForSeedInput(seedString);
                    nextKey = inputSource.getNextKey();
                    while (nextKey != 'S' && nextKey != 's') {
                        if (!validNumberInput.contains(nextKey)) {
                            StdDraw.text(0.5, 0.5, "WRONG INPUT TYPE!!!");
                            StdDraw.enableDoubleBuffering();
                            StdDraw.show();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        seedString += nextKey;
                        askingForSeedInput(seedString);
                        nextKey = inputSource.getNextKey();
                    }
                    this.seed = Long.parseLong(seedString);
                    existingInput += this.seed + ":";

                    TETile[][] blankWorld = new TETile[WIDTH][HEIGHT];
                    map = new WorldGenerator(seed, blankWorld);
                    world = map.getWorld();
                    gameStart = true;

                    displayGameState(world);
                } else if (nextKey == 'L' || nextKey == 'l') {
                    world = loadSavedWorld();

                    displayGameState(world);
                } else if (nextKey == 'Q' || nextKey == 'q') {
                    System.exit(0);
                }
            }

            existingInput += nextKey;
            if (nextKey == ':') {
                quitEnabled = true;
            }

            if (quitEnabled && (nextKey == 'Q' || nextKey == 'q')) {
                saveWorld(existingInput);
                System.exit(0);
            }
            if (!gameOver) {
                world = moveDirection(nextKey, map.getPlayer1());
                displayGameState(world);
            }

        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {

        InputSource inputSource = new StringInputDevice(input);
        String seedString = "";

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            if (!validInput.contains(nextKey)) {
                return null;
            }
            if (!gameStart) {
                if (nextKey == 'n' || nextKey == 'N') {
                    seedString += nextKey;
                    nextKey = inputSource.getNextKey();
                    while (nextKey != 'S' && nextKey != 's') {
                        seedString += nextKey;
                        nextKey = inputSource.getNextKey();
                    }
                    seedString += nextKey;
                    // TODO save seed somewhere
                    seed = Long.parseLong(seedString.substring(0, seedString.length() - 1));
                    world = new TETile[WIDTH][HEIGHT];
                    map = new WorldGenerator(seed, world);
                    gameStart = true;
                    existingInput += seed + ":";
                } else if (nextKey == 'l' || nextKey == 'L') {
                    world = loadSavedWorld();
                } else if (nextKey == ':') {
                    quitEnabled = true;
                    nextKey = inputSource.getNextKey();
                    if (nextKey == 'Q' || nextKey == 'q') {
                        saveWorld(existingInput);
                        // exit???
                    }
                }
            }
            existingInput += nextKey;
            world = moveDirection(nextKey, map.getPlayer1());
        }
        return world;
    }

    private TETile[][] moveDirection(char input, Avatar player) {
        TETile[][] worldAfterMoving = null;
        // directions
        if (input == 'W') {
            worldAfterMoving = map.movePlayer(player, world, "up");
        } else if (input == 'S') {
            worldAfterMoving = map.movePlayer(player, world, "down");
        } else if (input == 'A') {
            worldAfterMoving = map.movePlayer(player, world, "left");
        } else if (input == 'D') {
            worldAfterMoving = map.movePlayer(player, world, "right");
        }
        return worldAfterMoving;
    }

    private TETile[][] loadSavedWorld() {
        File file = new File("save.txt");
        if (!file.exists()) {
            return null;
        }
        try {
            Scanner scanner = new Scanner(new File("save.txt"));
            scanner.useDelimiter(":");
            String scannedSeed = "";
            String scannedMove = "";
            while (scanner.hasNext()) {
                scannedSeed = scanner.next();
                scannedMove = scanner.next();
                scanner.next();
            }
            TETile[][] savedWorld = null;
            existingInput = "N" + scannedSeed + "S" + scannedMove;
            savedWorld = interactWithInputString(existingInput);
            return savedWorld;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveWorld(String inputContent) {
        try {
            FileWriter fileWritter = new FileWriter("save.txt");
            fileWritter.write(inputContent);
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void displayMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setPenColor(new Color(171, 156, 115));
        StdDraw.filledSquare(0, 0, 1);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(0.5, 0.75, "CS61B: The Game");
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(0.5, 0.3, "New Game: (N)");
        StdDraw.text(0.5, 0.25, "Load Game: (L)");

        StdDraw.text(0.5, 0.15, "Quit: (Q)");
        StdDraw.text(0.5, 0.45,
                "Waiting to be implemented");
        StdDraw.text(0.5, 0.5,
                "waiting to be implemented");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    public void askingForSeedInput(String seedString) {
        // the interface to show the entering of seed.
        StdDraw.clear();
        StdDraw.setPenColor(new Color(171, 156, 115));
        StdDraw.filledSquare(0, 0, 1);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(0.5, 0.75, "Enter Random Seed:");
        StdDraw.text(0.5, 0.5, seedString);
        StdDraw.text(0.5, 0.25, "Press 'S' to Create World");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    public void displayGameState(TETile[][] world){
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

}