package ru.job4j.tictactoe;

import javafx.scene.shape.Rectangle;

/**
 * Table with cells for tic-tac-toe game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.04.2018
 */
public class Figure3T extends Rectangle {
    /**
     * Flag: is marked as "X".
     */
    private boolean markX = false;
    /**
     * Flag: is marked as "O".
     */
    private boolean markO = false;

    /**
     * Constructs empty cell (not marked).
     */
    public Figure3T() {
        super();
    }

    /**
     * Constructs cell marked by "X" or "O".
     *
     * @param markX if <tt>true</tt> - cell will be marked by "X", if <tt>false</tt> - cell will be marked by "O".
     */
    public Figure3T(boolean markX) {
        this.markX = markX;
        this.markO = !markX;
    }

    /**
     * Sets mark in cell.
     *
     * @param markX <tt>true</tt> means set "X", <tt>false</tt> means set "O.
     */
    public void setMark(boolean markX) {
        this.markX = markX;
        this.markO = !markX;
    }

    /**
     * Returns if cell is marked with "X".
     *
     * @return <tt>true</tt> if mark is "X", otherwise "false".
     */
    public boolean hasMarkX() {
        return this.markX;
    }

    /**
     * Returns if cell is marked with "O".
     *
     * @return <tt>true</tt> if mark is "O", otherwise "false".
     */
    public boolean hasMarkO() {
        return this.markO;
    }
}

