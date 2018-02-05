package ru.job4j.chessboard;

import java.util.Arrays;

class Rook extends Figure {

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
        //check
        if (source.getX() != dest.getX() && source.getY() != dest.getY()) {
            throw new ImpossibleMoveException("Rook figure cannot move like this.");
        }
        //direction
        boolean vertical = dest.getX() == source.getX();
        int step;
        if (vertical) {
            step = dest.getY() > source.getY() ? 1 : -1;
        } else {
            step = dest.getX() > source.getX() ? 1 : -1;
        }
        //move
        int wayX = vertical ? source.getX() : source.getX() + step;
        int wayY = vertical ? source.getY() + step : source.getY();
        int positionWay = 0;
        Cell[] tempWay = new Cell[100];
        while (vertical
                ? Math.abs(dest.getY() - wayY) != 0
                : Math.abs(dest.getX() - wayX) != 0
                ) {
            tempWay[positionWay++] = new Cell(wayX, wayY);
            wayX += vertical ? 0 : step;
            wayY += vertical ? step : 0;
        }
        //last step
        tempWay[positionWay++] = new Cell(wayX, wayY);
        return Arrays.copyOf(tempWay, positionWay);
    }

}
