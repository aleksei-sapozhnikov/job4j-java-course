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
     * Possible horizontal directions.
     */
    enum HorizontalDirection {
        NONE, LEFT, RIGHT
    }

    /**
     * Possible vertical directions.
     */
    enum VerticalDirection {
        NONE, UP, DOWN
    }

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
     * Calculates horizontal distance from this cell to another cell.
     *
     * @param other cell to calculate distance to.
     * @return positive distance if other cell horizontal coordinate is bigger then that of this cell,
     * negative distance otherwise, zero if horizontal coordinates are equal.
     */
    int horizontalDistanceTo(Cell other) {
        return other.x - this.x;
    }

    /**
     * Calculates vertical distance from this cell to another cell.
     *
     * @param other cell to calculate distance to.
     * @return positive distance if other cell vertical coordinate is bigger then that of this cell,
     * negative distance otherwise, zero if vertical coordinates are equal.
     */
    int verticalDistanceTo(Cell other) {
        return other.y - this.y;
    }

    /**
     * Returns a cell in specified direction, next to this cell.
     *
     * @param horizontal where to look in horizontal direction.
     * @param vertical where to look in vertical direction.
     * @return Next cell found.
     */
    Cell step(Enum horizontal, Enum vertical) {
        int xStep = horizontal == HorizontalDirection.NONE
                ? 0
                : horizontal == HorizontalDirection.RIGHT ? 1 : -1;
        int yStep = vertical == VerticalDirection.NONE
                ? 0
                : vertical == VerticalDirection.UP ? 1 : -1;
        return new Cell(this.x + xStep, this.y + yStep);
    }

    /**
     * Checks if two cells are equal.
     *
     * @param oth Another cell to check.
     * @return true if equal, false if not.
     */
    @Override
    public boolean equals(Object oth) {
        boolean result = false;
        if (oth instanceof Cell) {
            Cell other = (Cell) oth;
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

