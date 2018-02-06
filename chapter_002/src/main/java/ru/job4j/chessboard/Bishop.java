package ru.job4j.chessboard;

import java.util.Arrays;

/**
 * Bishop chess figure.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
class Bishop extends Figure {

    /**
     * Constructor.
     *
     * @param position Cell where the figure is.
     */
    Bishop(Cell position) {
        super(position);
    }

    /**
     * Creates figure in the cell.
     *
     * @param dest cell where to create figure.
     * @return Figure with new coordinates.
     */
    @Override
    Bishop copy(Cell dest) {
        return new Bishop(dest);
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
        if (Math.abs(source.horizontalDistanceTo(dest)) != Math.abs(source.verticalDistanceTo(dest))) {
            throw new ImpossibleMoveException("Bishop figure cannot move like this.");
        }
        //where to move
        Enum hrzDirection = source.horizontalDistanceTo(dest) > 0 ? Cell.HorizontalDirection.RIGHT : Cell.HorizontalDirection.LEFT;
        Enum vertDirection = source.verticalDistanceTo(dest) > 0 ? Cell.VerticalDirection.UP : Cell.VerticalDirection.DOWN;
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
