package ru.job4j.tictactoe.printer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.cell.ICell;

/**
 * Printer for TicTacToe game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Printer implements IPrinter {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Printer.class);

    /**
     * Returns current table status as string.
     *
     * @param table TicTacToe table.
     * @return Table status as string.
     */
    @Override
    public String print(ICell[][] table) {
        this.validateTable(table);
        var builder = new StringBuilder();
        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table[0].length; col++) {
                this.printCell(table[row][col], builder);
            }
            this.addRowDelimiterIfNeeded(row, table.length - 1, builder);
        }
        return builder.toString();
    }

    /**
     * Validates if given table is good to print.
     *
     * @param table Table.
     */
    private void validateTable(ICell[][] table) {
        if (table.length <= 0 || table[0].length <= 0) {
            throw new IllegalArgumentException("Table length and height must be > 0");
        }
        for (var row : table) {
            for (var cell : row) {
                if (cell == null) {
                    throw new IllegalArgumentException("Cells in the table must be != null");
                }
            }
        }
    }

    /**
     * Appends given cell status to string builder.
     *
     * @param cell    Cell to print.
     * @param builder String builder to append to.
     */
    private void printCell(ICell cell, StringBuilder builder) {
        if (cell.hasMarkX()) {
            builder.append("X");
        } else if (cell.hasMarkO()) {
            builder.append("O");
        } else {
            builder.append("-");
        }
    }

    /**
     * Adds row delimiter (line separator) if needed.
     *
     * @param row     Current row index.
     * @param lastRow Last row index.
     * @param builder Builder to append to.
     */
    private void addRowDelimiterIfNeeded(int row, int lastRow, StringBuilder builder) {
        if (row < lastRow) {
            builder.append(System.lineSeparator());
        }
    }
}
