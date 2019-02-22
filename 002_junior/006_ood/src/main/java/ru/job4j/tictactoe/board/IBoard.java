package ru.job4j.tictactoe.board;

import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.cell.ICell;

/**
 * Board holding and changing cells.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IBoard {
    /**
     * Sets X mark to cell with given coordinates.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    void setMarkX(int iHeight, int iWidth) throws WrongInputException;

    /**
     * Sets O mark to cell with given coordinates.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    void setMarkO(int iHeight, int iWidth) throws WrongInputException;

    /**
     * Checks if cell with given coordinates has mark X.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    boolean isMarkX(int iHeight, int iWidth) throws WrongInputException;

    /**
     * Checks if cell with given coordinates has mark O.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    boolean isMarkO(int iHeight, int iWidth) throws WrongInputException;

    /**
     * Returns table of cells.
     *
     * @return Table of cells.
     */
    ICell[][] getCells();
}
