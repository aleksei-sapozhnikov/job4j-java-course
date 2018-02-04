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
    private final int x;

    /**
     * Vertical coordinate (down --> up)
     */
    private final int y;

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
     * Get horizontal coordinate.
     *
     * @return horizontal coordinate field value.
     */
    int getX() {
        return this.x;
    }

    /**
     * Get vertical coordinate.
     *
     * @return vertical coordinate field value.
     */
    int getY() {
        return this.y;
    }

    /**
     * Checks if cell coordinates are in the defined square range.
     *
     * @param xMin lower boundary of the range in horizontal axis.
     * @param xMax higher boundary of the range in horizontal axis.
     * @param yMin lower boundary of the range in vertical axis.
     * @param yMax higher boundary of the range in horizontal axis.
     * @return true if position is in range, false if out of range.
     */
    boolean isInRange(int xMin, int xMax, int yMin, int yMax) {
        return this.x >= xMin
                && this.x <= xMax
                && this.y >= yMin
                && this.y <= yMax;
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

    /**
     * Calculates hashCode of the object.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return 31 * (this.x + this.y);
    }
}
