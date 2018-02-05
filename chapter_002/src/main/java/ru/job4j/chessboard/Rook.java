package ru.job4j.chessboard;

import java.util.Arrays;

public class Rook extends Figure {

    /**
     * Constructor.
     *
     * @param position Cell where the figure is.
     */
    Rook(Cell position) {
        super(position);
    }

    /**
     * Creates figure in the cell.
     *
     * @param dest cell where to create figure.
     * @return Figure with new coordinates.
     */
    @Override
    Rook copy(Cell dest) {
        return new Rook(dest);
    }

    /**
     * Returns an array of cells which figure will pass to go to destination cell.
     *
     * @param source Cell where figure starts from.
     * @param dest   Cell where we want it to go.
     * @return Array of cells which figure will pass going from source cell to destination.
     * @throws ImpossibleMoveException if figure movement cannot reach destination position.
     */
    @Override
    Cell[] way(Cell source, Cell dest) throws ImpossibleMoveException {
        //check where to go
        int step;
        boolean vertical;
        if (dest.getX() == source.getX() && dest.getY() != source.getY()) {
            vertical = true;
            step = dest.getX() > source.getX() ? 1 : -1;
        }


        if (dest.getX() != source.getX() && dest.getY() != dest.getY()) {
            throw new ImpossibleMoveException("Rook figure cannot move like this.");
        }
        //move
        boolean goRight = dest.getX() > source.getX();
        boolean goingUp = dest.getY() > source.getY();
        int dx = goRight ? 1 : -1;
        int dy = goingUp ? 1 : -1;
        int wayX = goRight ? source.getX() + 1 : source.getX() - 1;
        int wayY = goingUp ? source.getY() + 1 : source.getY() - 1;
        int positionWay = 0;
        Cell[] tempWay = new Cell[100];
        while (goRight
                ? wayX <= dest.getX()
                : wayX >= dest.getX()
                ) {
            tempWay[positionWay++] = new Cell(wayX, wayY);
            wayX += dx;
            wayY += dy;
        }
        return Arrays.copyOf(tempWay, positionWay);
    }

}
