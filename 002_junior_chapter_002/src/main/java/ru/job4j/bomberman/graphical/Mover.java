package ru.job4j.bomberman.graphical;

import javafx.scene.shape.Rectangle;
import ru.job4j.bomberman.Personage;

public class Mover implements Runnable {
    private final Personage personage;
    private final Rectangle rectangle;
    private final GraphicalCell[][] cells;
    private final int cellSize;
    boolean working;

    public Mover(Personage personage, Rectangle rectangle, GraphicalCell[][] cells, int cellSize) {
        this.personage = personage;
        this.rectangle = rectangle;
        this.cells = cells;
        this.cellSize = cellSize;
        this.working = true;
    }

    @Override
    public void run() {
        try {
            while (working && !Thread.currentThread().isInterrupted()) {
                for (int y = 0; y < this.cells[0].length; y++) {
                    for (int x = 0; x < this.cells.length; x++) {
                        this.moveTo(x, y);
                        Thread.sleep(750);
                    }
                }
            }
        } catch (InterruptedException e) {
            this.working = false;
        } finally {
            this.working = false;
        }
    }

    private void moveTo(int x, int y) {
        double toX = this.cells[x][y].x() + (this.cellSize - this.rectangle.getWidth()) / 2;
        double toY = this.cells[x][y].y() + (this.cellSize - this.rectangle.getHeight()) / 2;
        this.rectangle.setX(toX);
        this.rectangle.setY(toY);
    }
}
