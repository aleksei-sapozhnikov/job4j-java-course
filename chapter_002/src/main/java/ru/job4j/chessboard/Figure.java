package ru.job4j.chessboard;

/**
 * Common class for chess figures.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
abstract class Figure {

    /**
     * Position of the figure on the board.
     */
    private final Cell position;

    /**
     * Constructor. Creates figure in this position.
     *
     * @param position Cell where the figure is created.
     */
    Figure(Cell position) {
        this.position = position;
    }

    /**
     * Checks if figure is in cell.
     *
     * @param cell cell where to check.
     */
    boolean isInPosition(Cell cell) {
        return this.position.equals(cell);
    }

    /**
     * Creates figure in the cell.
     *
     * @param dest cell where to create figure.
     * @return new figure with coordinates of the destination cell.
     */
    abstract Figure copy(Cell dest);

    /**
     * Returns an array of cells which figure will pass to go to destination cell.
     *
     * @param source Cell where figure starts from.
     * @param dest   Cell where we want it to go.
     * @return Array of cells which figure will pass going from source cell to destination.
     * @throws ImpossibleMoveException if figure movement cannot reach destination position.
     */
    abstract Cell[] way(Cell source, Cell dest) throws ImpossibleMoveException;


}
