package ru.job4j.tictactoe.winchecker;

import ru.job4j.tictactoe.cell.ICell;

/**
 * Logical operations for tic-tac-toe game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.04.2018
 */
public class DoLineWinChecker implements IWinChecker {

    /**
     * Winning combination size.
     */
    private final int size;

    /**
     * Constructs new winning logic.
     *
     * @param size Winning combination size (line length).
     */
    public DoLineWinChecker(int size) {
        this.size = size;
    }

    /**
     * Checks if "X" won the game.
     *
     * @return <tt>true</tt> if "X" won, <tt>false</tt> if not.
     */
    @Override
    public boolean isWinnerX(ICell[][] table) {
        return this.checkWinner(table, true);
    }

    /**
     * Checks if "O" won the game.
     *
     * @return <tt>true</tt> if "O" won, <tt>false</tt> if not.
     */
    @Override
    public boolean isWinnerO(ICell[][] table) {
        return this.checkWinner(table, false);
    }

    /**
     * Checks if conditions are met to win for given mark.
     *
     * @param hasMarkX check for "X" mark, else for "O" mark.
     * @return <tt>true</tt> if conditions are met, <tt>false</tt> if not.
     */
    private boolean checkWinner(ICell[][] table, boolean hasMarkX) {
        if (table.length <= 0) {
            throw new IllegalArgumentException("Table length must be > 0");
        }
        var toLine = this.size - 1;
        var result = this.checkHorizontalLines(table, hasMarkX, toLine);
        result = result || this.checkVerticalLines(table, hasMarkX, toLine);
        result = result || this.checkDiagonalsDownLeft(table, hasMarkX, toLine);
        result = result || this.checkDiagonalsUpRight(table, hasMarkX, toLine);
        return result;
    }

    /**
     * Checks if any of up-right diagonals has winning combination.
     *
     * @param table    Table of cells.
     * @param hasMarkX if <tt>true</tt> check X marks, if <tt>false</tt> check O marks.
     * @param toLine   We need to add this amount of cells to current one to get winning line.
     * @return <tt>true</tt> if found winning combination, <tt>false</tt> if not.
     */
    private boolean checkDiagonalsUpRight(ICell[][] table, boolean hasMarkX, int toLine) {
        boolean result = false;
        for (int row = table.length - 1; !result && row - toLine >= 0; row--) {
            for (int col = 0; !result && col + toLine < table.length; col++) {
                result = this.isDiagonal(table, row, col, row - toLine, col + toLine, hasMarkX);
            }
        }
        return result;
    }

    /**
     * Checks if any of down-left diagonals has winning combination.
     *
     * @param table    Table of cells.
     * @param hasMarkX if <tt>true</tt> check X marks, if <tt>false</tt> check O marks.
     * @param toLine   We need to add this amount of cells to current one to get winning line.
     * @return <tt>true</tt> if found winning combination, <tt>false</tt> if not.
     */
    private boolean checkDiagonalsDownLeft(ICell[][] table, boolean hasMarkX, int toLine) {
        var result = false;
        for (int row = 0; !result && row + toLine < table.length; row++) {
            for (int col = 0; !result && col + toLine < table[0].length; col++) {
                result = this.isDiagonal(table, row, col, row + toLine, col + toLine, hasMarkX);
            }
        }
        return result;
    }

    /**
     * Checks if any of vertical lines has winning combination.
     *
     * @param table    Table of cells.
     * @param hasMarkX if <tt>true</tt> check X marks, if <tt>false</tt> check O marks.
     * @param toLine   We need to add this amount of cells to current one to get winning line.
     * @return <tt>true</tt> if found winning combination, <tt>false</tt> if not.
     */
    private boolean checkVerticalLines(ICell[][] table, boolean hasMarkX, int toLine) {
        var result = false;
        for (int col = 0; !result && col < table[0].length; col++) {
            for (int row = 0; !result && row + toLine < table.length; row++) {
                result = this.isLine(table, row, col, row + toLine, false, hasMarkX);
            }
        }
        return result;
    }

    /**
     * Checks if any of horizontal lines has winning combination.
     *
     * @param table    Table of cells.
     * @param hasMarkX if <tt>true</tt> check X marks, if <tt>false</tt> check O marks.
     * @param toLine   We need to add this amount of cells to current one to get winning line.
     * @return <tt>true</tt> if found winning combination, <tt>false</tt> if not.
     */
    private boolean checkHorizontalLines(ICell[][] table, boolean hasMarkX, int toLine) {
        var result = false;
        for (int row = 0; !result && row < table.length; row++) {
            for (int col = 0; !result && col + toLine < table[0].length; col++) {
                result = this.isLine(table, row, col, col + toLine, true, hasMarkX);
            }
        }
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
    private boolean isLine(ICell[][] table, int xStart, int yStart, int end, boolean horizontal, boolean hasMarkX) {
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
    private boolean isDiagonal(ICell[][] table, int xStart, int yStart, int xEnd, int yEnd, boolean hasMarkX) {
        boolean result = Math.abs(xStart - xEnd) == Math.abs(yStart - yEnd);
        if (result) {
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
        }
        return result;
    }

    /**
     * Checks if there is a field without mark on the table.
     *
     * @return <tt>true</tt> if found empty cell, <tt>false</tt> if not found.
     */
    @Override
    public boolean hasGap(ICell[][] table) {
        boolean result = false;
        out:
        for (ICell[] line : table) {
            for (ICell cell : line) {
                if (!cell.hasMarkX() && !cell.hasMarkO()) {
                    result = true;
                    break out;
                }
            }
        }
        return result;
    }

}

