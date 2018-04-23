package ru.job4j.tictactoe;

/**
 * Logical operations for tic-tac-toe game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.04.2018
 */
public class Logic3T {
    /**
     * Play table.
     */
    private final Figure3T[][] table;

    /**
     * Constructs new Logic object for a given play table.
     *
     * @param table play table.
     */
    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    /**
     * Checks if "X" won the game.
     *
     * @return <tt>true</tt> if "X" won, <tt>false</tt> if not.
     */
    public boolean isWinnerX() {
        return this.checkWinner(true);
    }

    /**
     * Checks if "O" won the game.
     *
     * @return <tt>true</tt> if "O" won, <tt>false</tt> if not.
     */
    public boolean isWinnerO() {
        return this.checkWinner(false);
    }

    /**
     * Checks if conditions are met to win for given mark.
     *
     * @param hasMarkX check for "X" mark, else for "O" mark.
     * @return <tt>true</tt> if conditions are met, <tt>false</tt> if not.
     */
    private boolean checkWinner(boolean hasMarkX) {
        boolean result = false;
        for (int i = 0; !result && i < table.length; i++) {
            result = this.isLine(i, 0, table.length - 1, true, hasMarkX);
        }
        for (int i = 0; !result && i < table[0].length; i++) {
            result = this.isLine(0, i, table.length - 1, false, hasMarkX);
        }
        result = result || this.isDiagonal(0, 0, table.length - 1, table.length - 1, hasMarkX);
        result = result || this.isDiagonal(table.length - 1, 0, 0, table.length - 1, hasMarkX);
        return result;
    }

    /**
     * Checks if line (horizontal or vertical) between given coordinates is filled with "X" of "O".
     *
     * @param xStart     x coordinate of start (line).
     * @param yStart     y coordinate of start (row).
     * @param end        coordinate of finish (x if vertical line, y if horizontal).
     * @param horizontal <tt>true</tt></tt> means line is horizontal, <tt>false</tt> means vertical line.
     * @param hasMarkX   <tt>true</tt> to check for "X" mark, <tt>false</tt> to check for "O".
     * @return <tt>true</tt> if line is filled with mark, <tt>false</tt> otherwise.
     */
    private boolean isLine(int xStart, int yStart, int end, boolean horizontal, boolean hasMarkX) {
        boolean result = true;
        int x = xStart;
        int y = yStart;
        while (horizontal ? y <= end : x <= end) {
            boolean condition = hasMarkX ? table[x][y].hasMarkX() : table[x][y].hasMarkO();
            if (!condition) {
                result = false;
                break;
            }
            x = horizontal ? x : x + 1;
            y = horizontal ? y + 1 : y;
        }
        return result;
    }

    /**
     * Checks if diagonal line (horizontal or vertical) between given coordinates is filled with "X" of "O".
     *
     * @param xStart   x coordinate of start (line).
     * @param yStart   y coordinate of start (row).
     * @param xEnd     x coordinate of end (line).
     * @param yEnd     y coordinate of end (row).
     * @param hasMarkX <tt>true</tt> to check for "X" mark, <tt>false</tt> to check for "O".
     * @return <tt>true</tt> if line is filled with mark, <tt>false</tt> otherwise.
     */
    boolean isDiagonal(int xStart, int yStart, int xEnd, int yEnd, boolean hasMarkX) {
        if (Math.abs(xStart - xEnd) != Math.abs(yStart - yEnd)) {
            throw new RuntimeException("No diagonal by given coordinates");
        }
        boolean result = true;
        boolean downRight = xEnd > xStart;
        int x = xStart;
        int y = yStart;
        while (y <= yEnd) {
            boolean condition = hasMarkX ? table[x][y].hasMarkX() : table[x][y].hasMarkO();
            if (!condition) {
                result = false;
                break;
            }
            x = downRight ? x + 1 : x - 1;
            y++;
        }
        return result;
    }

    /**
     * Checks if there is a field without mark on the table.
     *
     * @return <tt>true</tt> if found empty cell, <tt>false</tt> if not found.
     */
    public boolean hasGap() {
        boolean result = false;
        out:
        for (Figure3T[] line : table) {
            for (Figure3T cell : line) {
                if (!cell.hasMarkX() && !cell.hasMarkO()) {
                    result = true;
                    break out;
                }
            }
        }
        return result;
    }

}

