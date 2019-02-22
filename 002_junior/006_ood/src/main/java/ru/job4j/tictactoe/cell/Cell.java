package ru.job4j.tictactoe.cell;

/**
 * Simple TicTacToe cell object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum Cell implements ICell, ICellFactory {
    /**
     * Not marked cell object.
     */
    FREE("-"),
    /**
     * Cell marked with X.
     */
    MARK_X("X"),
    /**
     * Cell marked with O.
     */
    MARK_O("O");

    /**
     * Flag showing cell is marked by X.
     */
    private final boolean markX;
    /**
     * Flag showing cell is marked by O.
     */
    private final boolean markO;

    /**
     * Constructs new instance.
     *
     * @param type Cell type.
     */
    Cell(String type) {
        if ("X".equals(type)) {
            this.markX = true;
            this.markO = false;
        } else if ("O".equals(type)) {
            this.markX = false;
            this.markO = true;
        } else {
            this.markX = false;
            this.markO = false;
        }
    }

    /**
     * Returns free cell.
     *
     * @return Free cell.
     */
    @Override
    public ICell getFree() {
        return Cell.FREE;
    }

    /**
     * Returns cell with X mark
     *
     * @return Cell with X mark.
     */
    @Override
    public ICell getMarkX() {
        return Cell.MARK_X;
    }

    /**
     * Returns cell with O mark
     *
     * @return Cell with O mark.
     */
    @Override
    public ICell getMarkO() {
        return Cell.MARK_O;
    }

    /**
     * Returns if cell is marked with "X".
     *
     * @return <tt>true</tt> if mark is "X", otherwise "false".
     */
    @Override
    public boolean hasMarkX() {
        return this.markX;
    }

    /**
     * Returns if cell is marked with "O".
     *
     * @return <tt>true</tt> if mark is "O", otherwise "false".
     */
    @Override
    public boolean hasMarkO() {
        return this.markO;
    }
}
