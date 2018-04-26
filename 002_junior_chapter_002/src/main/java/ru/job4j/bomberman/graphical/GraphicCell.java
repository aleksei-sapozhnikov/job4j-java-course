package ru.job4j.bomberman.graphical;

import javafx.scene.shape.Rectangle;

public class GraphicCell extends Rectangle {
    private final int x;
    private final int y;

    public GraphicCell(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
}

