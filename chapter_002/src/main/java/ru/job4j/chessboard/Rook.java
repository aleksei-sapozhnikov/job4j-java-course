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
        if (source.horizontalDistanceTo(dest) != 0 && source.verticalDistanceTo(dest) != 0) {
            throw new ImpossibleMoveException("Rook figure cannot move like this.");
        }
        //direction
        Enum hrzDirection;
        Enum vertDirection;
        if (source.horizontalDistanceTo(dest) == 0) {
            hrzDirection = Cell.HorizontalDirection.NONE;
            vertDirection = source.verticalDistanceTo(dest) > 0 ? Cell.VerticalDirection.UP : Cell.VerticalDirection.DOWN;
        } else {
            vertDirection = Cell.VerticalDirection.NONE;
            hrzDirection = source.horizontalDistanceTo(dest) > 0 ? Cell.HorizontalDirection.RIGHT : Cell.HorizontalDirection.LEFT;
        }
        //move
        Cell[] tempWay = new Cell[100];
        int positionWay = 0;
        Cell wayCell = source.step(hrzDirection, vertDirection);
        while (!wayCell.equals(dest)) {
            tempWay[positionWay++] = wayCell;
            wayCell = wayCell.step(hrzDirection, vertDirection);
        }
        //last step
        tempWay[positionWay++] = wayCell;
        //result
        return Arrays.copyOf(tempWay, positionWay);
    }

}
