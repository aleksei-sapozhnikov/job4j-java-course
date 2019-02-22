package ru.job4j.tictactoe.printer;

import ru.job4j.tictactoe.cell.ICell;

public interface IPrinter {
    /**
     * Returns current table status as string.
     *
     * @param table TicTacToe table.
     * @return Table status as string.
     */
    String print(ICell[][] table);
}
