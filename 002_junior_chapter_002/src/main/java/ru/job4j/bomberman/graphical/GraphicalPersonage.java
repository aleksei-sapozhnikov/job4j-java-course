package ru.job4j.bomberman.graphical;

import javafx.scene.shape.Rectangle;
import ru.job4j.bomberman.Board;
import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.Personage;
import ru.job4j.bomberman.WrongCoordinatesException;

public class GraphicalPersonage {
    private final Board board;
    private final Personage personage;
    private final Rectangle rectangle;
    private final GraphicalCell[][] cells;
    private final int cellSize;

    public GraphicalPersonage(Board board, Personage personage, Rectangle rectangle, GraphicalCell[][] cells, int cellSize) {
        this.board = board;
        this.personage = personage;
        this.rectangle = rectangle;
        this.cells = cells;
        this.cellSize = cellSize;
    }

    public void place() throws WrongCoordinatesException {
        this.personage.place();
        this.graphicTo(this.personage.x(), this.personage.y());
    }

    public GraphicalPersonage nextMove() throws InterruptedException, WrongCoordinatesException {
        boolean moved = false;
        try {
            this.board.lock(this.personage.x(), this.personage.y());
            Personage after = this.personage.nextMove(Direction.values());
            GraphicalPersonage result = new GraphicalPersonage(this.board, after, this.rectangle, this.cells, this.cellSize);
            this.graphicTo(after.x(), after.y());
            moved = true;
            return result;
        } finally {
            if (moved) {
                this.board.unlock(this.personage.x(), this.personage.y());
            }
        }
    }

    public GraphicalPersonage tryMove(Direction direction) throws InterruptedException, WrongCoordinatesException {
        boolean moved = false;
        GraphicalPersonage result = this;
        try {
            this.board.lock(this.personage.x(), this.personage.y());
            Personage after = this.personage.tryMove(direction);
            if (after != this.personage) {
                result = new GraphicalPersonage(this.board, after, this.rectangle, this.cells, this.cellSize);
            }
            moved = true;
            return result;
        } finally {
            if (moved) {
                result.graphicTo(result.personage.x(), result.personage.y());
                this.board.unlock(this.personage.x(), this.personage.y());
            }
        }
    }

    public void graphicTo(int x, int y) {
        double toX = this.cells[x][y].x() + (this.cellSize - this.rectangle.getWidth()) / 2;
        double toY = this.cells[x][y].y() + (this.cellSize - this.rectangle.getHeight()) / 2;
        this.rectangle.setX(toX);
        this.rectangle.setY(toY);
    }
}
