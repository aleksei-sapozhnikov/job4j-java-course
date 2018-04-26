package ru.job4j.bomberman.graphical;

import javafx.scene.shape.Rectangle;
import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.Personage;
import ru.job4j.bomberman.WrongCoordinatesException;

public class GraphicalPersonage {
    private final Personage personage;
    private final Rectangle rectangle;
    private final GraphicalCell[][] cells;
    private final int cellSize;

    public GraphicalPersonage(Personage personage, Rectangle rectangle, GraphicalCell[][] cells, int cellSize) {
        this.personage = personage;
        this.rectangle = rectangle;
        this.cells = cells;
        this.cellSize = cellSize;
    }

    public void init() throws WrongCoordinatesException {
        this.personage.init();
        this.moveTo(this.personage.x(), this.personage.y());
    }

    public GraphicalPersonage move(Direction direction) throws InterruptedException {
        GraphicalPersonage result = this;
        Personage moved = this.personage.move(direction);
        if (moved != this.personage) {
            result = new GraphicalPersonage(moved, this.rectangle, this.cells, this.cellSize);
            this.moveTo(moved.x(), moved.y());
        }
        return result;
    }

    public void moveTo(int x, int y) {
        double toX = this.cells[x][y].x() + (this.cellSize - this.rectangle.getWidth()) / 2;
        double toY = this.cells[x][y].y() + (this.cellSize - this.rectangle.getHeight()) / 2;
        this.rectangle.setX(toX);
        this.rectangle.setY(toY);
    }

}
