package byow.Core;

public interface Structure {
    int height = 0;
    int width = 0;
    int up = 0;
    int down = 0;

    default int getHeight() {
        return height;
    }

    default int getWidth() {
        return width;
    }

    default int getUp() {
        return up;
    }

    default int getDown() {
        return down;
    }
}
