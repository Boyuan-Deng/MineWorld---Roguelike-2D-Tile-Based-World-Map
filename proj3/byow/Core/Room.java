package byow.Core;

public class Room{
    private int height;
    private int width;
    private int xCoordinate;
    private int yCoordinate;
    public Room(int height, int width, int x, int y) {
        this.height = height;
        this.width = width;
        this.xCoordinate = x;
        this.yCoordinate = y;
    }


    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
