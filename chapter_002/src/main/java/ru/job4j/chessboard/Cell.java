package ru.job4j.chessboard;

/**
 * Describes one cell of the chessboard.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
class Cell {

    /**
     * Horizontal coordinate (left -> right)
     */
    final int x;

    /**
     * Vertical coordinate (down --> up)
     */
    final int y;

    /**
     * Constructor.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if two cells are equal.
     *
     * @param otherObject Another cell to check.
     * @return true if equal, false if not.
     */
    @Override
    public boolean equals(Object otherObject) {
        boolean result = false;
        if (otherObject instanceof Cell) {
            Cell other = (Cell) otherObject;
            if (this.x == other.x && this.y == other.y) {
                result = true;
            }
        }
        return result;
    }
}
