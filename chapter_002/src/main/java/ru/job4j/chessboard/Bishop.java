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
        if (Math.abs(dest.getX() - source.getX()) != Math.abs(dest.getY() - source.getY())) {
            throw new ImpossibleMoveException("Bishop figure cannot move like this.");
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
