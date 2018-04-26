package ru.job4j.bomberman.graphical;

import javafx.scene.shape.Rectangle;
import ru.job4j.bomberman.Character;

public class Mover implements Runnable {
    private final Character character;
    private final Rectangle rect;
    private final GraphicCell[][] cells;
    private final int cellSize;

    public Mover(Character character, Rectangle rect, GraphicCell[][] cells, int cellSize) {
        this.character = character;
        this.rect = rect;
        this.cells = cells;
        this.cellSize = cellSize;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int y = 0; y < this.cells[0].length; y++) {
                    for (int x = 0; x < this.cells.length; x++) {
                        this.moveTo(x, y);
                        Thread.sleep(750);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveTo(int x, int y) {
        double toX = this.cells[x][y].x() + (this.cellSize - this.rect.getWidth()) / 2;
        double toY = this.cells[x][y].y() + (this.cellSize - this.rect.getHeight()) / 2;
        this.rect.setX(toX);
        this.rect.setY(toY);
    }
}
