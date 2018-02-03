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
        boolean goUp = dest.getX() > source.getX();
        int posWay = 0;
        Cell[] tempWay = new Cell[100];
        int wayX = goUp ? source.getX() + 1 : source.getX() - 1;
        int wayY = goUp ? source.getY() + 1 : source.getY() - 1;
        while (goUp
                ? wayX <= dest.getX()
                : wayX >= dest.getX()) {
            tempWay[posWay++] = goUp
                    ? new Cell(wayX++, wayY++)
                    : new Cell(wayX--, wayY--);
        }
        return Arrays.copyOf(tempWay, posWay);
    }


}
