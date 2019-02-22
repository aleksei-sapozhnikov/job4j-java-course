package ru.job4j.tictactoe.board;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.cell.ICell;
import ru.job4j.tictactoe.cell.ICellFactory;

/**
 * Board holding and changing cells.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Board implements IBoard {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Board.class);

    /**
     * Play table.
     */
    private final ICell[][] table;

    /**
     * Cell factory. Produces cell objects.
     */
    private final ICellFactory factory;

    /**
     * Constructs new instance.
     *
     * @param height  Board height.
     * @param width   Board width.
     * @param factory Cell factory.
     */
    public Board(int height, int width, ICellFactory factory) {
        this.table = new ICell[height][width];
        for (int i = 0; i < this.table.length; i++) {
            for (int j = 0; j < this.table[0].length; j++) {
                this.table[i][j] = factory.getFree();
            }
        }
        this.factory = factory;
    }

    /**
     * Sets X mark to cell with given coordinates.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    @Override
    public void setMarkX(int iHeight, int iWidth) throws WrongInputException {
        this.checkBounds(iHeight, iWidth);
        this.table[iHeight][iWidth] = this.factory.getMarkX();
    }

    /**
     * Sets O mark to cell with given coordinates.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    @Override
    public void setMarkO(int iHeight, int iWidth) throws WrongInputException {
        this.checkBounds(iHeight, iWidth);
        this.table[iHeight][iWidth] = this.factory.getMarkO();
    }

    /**
     * Checks if cell with given coordinates has mark X.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    @Override
    public boolean isMarkX(int iHeight, int iWidth) throws WrongInputException {
        this.checkBounds(iHeight, iWidth);
        return this.table[iHeight][iWidth].hasMarkX();
    }

    /**
     * Checks if cell with given coordinates has mark O.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    @Override
    public boolean isMarkO(int iHeight, int iWidth) throws WrongInputException {
        this.checkBounds(iHeight, iWidth);
        return this.table[iHeight][iWidth].hasMarkO();
    }

    /**
     * Returns table of cells.
     *
     * @return Table of cells.
     */
    @Override
    public ICell[][] getCells() {
        return this.table;
    }

    /**
     * Checks if given coordinates are in board bounds.
     *
     * @param iHeight Height index.
     * @param iWidth  Width index.
     * @throws WrongInputException If given coordinates are out of board bounds.
     */
    private void checkBounds(int iHeight, int iWidth) throws WrongInputException {
        if (iHeight >= this.table.length || iWidth >= this.table[0].length) {
            throw new WrongInputException("Target cell is out of bounds");
        }
    }
}
